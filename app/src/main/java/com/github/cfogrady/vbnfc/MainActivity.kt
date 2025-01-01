package com.github.cfogrady.vbnfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.cfogrady.vbnfc.data.DeviceType
import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.ui.theme.LibVbNfcExampleTheme
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var deviceToCryptographicTransformers: Map<UShort, CryptographicTransformer>

    private var nfcCharacter = MutableStateFlow<NfcCharacter?>(null)

    @OptIn(ExperimentalStdlibApi::class)
    override fun onNewIntent(intent: Intent?) {
        Log.i("MainActivity", "Intent: ${intent?.action}")
        super.onNewIntent(intent)
        if(NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            Log.i("MainActivity", "Tag Id Discovered: ${
                intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)?.toHexString()}")
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceToCryptographicTransformers = getMapOfCryptographicTransformers()

        val maybeNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (maybeNfcAdapter == null) {
            Toast.makeText(this, "No NFC on device!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        nfcAdapter = maybeNfcAdapter
        setContent {
            var phase by remember { mutableStateOf("") }
            var charIndex by remember { mutableStateOf("") }
            LibVbNfcExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row {
                            Text(text = "Phase")
                            TextField(value = phase, onValueChange = {
                                phase = it
                            })
                        }
                        Row {
                            Text(text = "Char Index")
                            TextField(value = charIndex, onValueChange = {
                                charIndex = it
                            })
                        }
                        Button(onClick = {
                            handleTag() {
                                val character = it.receiveCharacter()
                                nfcCharacter.value = character
                                phase=character.phase.toString()
                                charIndex=character.charIndex.toString()
                                "Done reading character"
                            }
                        }) {
                            Text(text = "Read Character")
                        }
                        Button(onClick = {
                            handleTag() {
                                nfcCharacter.value?.let { character ->
                                    Log.i("MainActivity", "Set Prepare dim for ${character.dimId}")
                                    it.prepareDIMForCharacter(character.dimId)
                                }
                                "Send character when device is ready"
                            }
                        }) {
                            Text(text = "Prepare Device For DIM")
                        }
                        Button(onClick = {
                            handleTag() {
                                nfcCharacter.value?.let {character ->
                                    character.phase = phase.toByte()
                                    character.charIndex = charIndex.toUShort()
                                    it.sendCharacter(character)
                                }
                                "Character sent"
                            }
                        }) {
                            Text(text = "Send Character")
                        }
                    }

                }
            }
        }
    }

    fun getMapOfCryptographicTransformers(): Map<UShort, CryptographicTransformer> {
        return mapOf(
            Pair(DeviceType.VitalBraceletBEDeviceType,
                CryptographicTransformer(salt1 = resources.getString(R.string.password1),
                    salt2 = resources.getString(R.string.password2),
                    decryptionKey = resources.getString(R.string.decryptionKey),
                    substitutionCipher = resources.getIntArray(R.array.substitutionArray))),
            Pair(DeviceType.VitalSeriesDeviceType,
                CryptographicTransformer(salt1 = resources.getString(R.string.password1),
                    salt2 = resources.getString(R.string.password2),
                    decryptionKey = resources.getString(R.string.decryptionKey),
                    substitutionCipher = resources.getIntArray(R.array.substitutionArray))),
            Pair(DeviceType.VitalCharactersDeviceType,
                CryptographicTransformer(salt1 = resources.getString(R.string.password1),
                    salt2 = resources.getString(R.string.password2),
                    decryptionKey = resources.getString(R.string.decryptionKey),
                    substitutionCipher = resources.getIntArray(R.array.substitutionArray)))
        )
    }

    private fun showWirelessSettings() {
        Toast.makeText(this, "NFC must be enabled", Toast.LENGTH_SHORT).show()
        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    }

    private fun buildOnReadTag(handlerFunc: (TagCommunicator)->String): (Tag)->Unit {
        return { tag->
            val nfcData = NfcA.get(tag)
            if (nfcData == null) {
                runOnUiThread {
                    Toast.makeText(this, "Tag detected is not VB", Toast.LENGTH_SHORT).show()
                }
            }
            nfcData.connect()
            nfcData.use {
                val tagCommunicator = TagCommunicator.getInstance(nfcData, deviceToCryptographicTransformers)
                val successText = handlerFunc(tagCommunicator)
                runOnUiThread {
                    Toast.makeText(this, successText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleTag(handlerFunc: (TagCommunicator)->String) {
        if (!nfcAdapter.isEnabled) {
            showWirelessSettings()
        } else {
            val options = Bundle()
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
            nfcAdapter.enableReaderMode(this, buildOnReadTag(handlerFunc), NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                options
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter.isEnabled) {
            nfcAdapter.disableReaderMode(this)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibVbNfcExampleTheme {
        Greeting("Android")
    }
}