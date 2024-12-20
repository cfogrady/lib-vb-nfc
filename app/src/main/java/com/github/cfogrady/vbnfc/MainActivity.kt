package com.github.cfogrady.vbnfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
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
import com.github.cfogrady.vbnfc.handlers.VBNfcHandler
import com.github.cfogrady.vbnfc.handlers.VBNfcHandlerFactory
import com.github.cfogrady.vbnfc.ui.theme.LibVbNfcExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope


class MainActivity : ComponentActivity() {

    val nfcCharacterFactory = NfcCharacterFactory()

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var secrets: VBNfcHandler.Secrets
    private lateinit var vbNfcHandlerFactory: VBNfcHandlerFactory

    private val dummyCharacter = NfcCharacter(
        132u,
        4u,
        NfcCharacter.AbilityRarity.None,
        0u,
        0u,
        0u,
        0u,
        0u,
        0u,
        0u,
        0u
    )

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
        secrets = VBNfcHandler.Secrets(
            passwordKey1 = resources.getString(R.string.password1),
            passwordKey2 = resources.getString(R.string.password2),
            decryptionKey = resources.getString(R.string.decryptionKey),
            substitutionCypher = resources.getIntArray(R.array.substitutionArray)
        )
        Log.i("MainActivity", "Secrets: $secrets")
        vbNfcHandlerFactory = VBNfcHandlerFactory(secrets, secrets, secrets)

        val maybeNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (maybeNfcAdapter == null) {
            Toast.makeText(this, "No NFC on device!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        nfcAdapter = maybeNfcAdapter
        setContent {
            var passwordKey1 by remember { mutableStateOf(secrets.passwordKey1) }
            var passwordKey2 by remember { mutableStateOf(secrets.passwordKey2) }
            var decryptionKey by remember { mutableStateOf(secrets.decryptionKey) }
            var dimId by remember { mutableStateOf("132") }
            LibVbNfcExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row {
                            Text(text = "Password Key 1")
                            TextField(value = passwordKey1, onValueChange = {
                                passwordKey1 = it
                            })
                        }
                        Row {
                            Text(text = "Password Key 2")
                            TextField(value = passwordKey2, onValueChange = {
                                passwordKey2 = it
                            })
                        }
                        Row {
                            Text(text = "Decryption Key")
                            TextField(value = decryptionKey, onValueChange = {
                                decryptionKey = it
                            })
                        }
                        Row {
                            Text(text = "DIM Id")
                            TextField(value = dimId, onValueChange = {
                                dimId = it
                            })
                        }
                        Button(onClick = {
                            secrets = VBNfcHandler.Secrets(passwordKey1, passwordKey2, decryptionKey, secrets.substitutionCypher)
                            vbNfcHandlerFactory = VBNfcHandlerFactory(secrets, secrets, secrets)
                        }) {
                            Text(text = "Regenerate NFC Handler From Keys")
                        }
                        Button(onClick = {
                            handleTag() {
                                it.receiveCharacter()
                                "Done reading character"
                            }
                        }) {
                            Text(text = "Read Character")
                        }
                        Button(onClick = {
                            handleTag() {
                                Log.i("MainActivity", "Set Prepare dim for ${dimId.toUShort()}")
                                it.prepareDIMForCharacter(dimId.toUShort())
                                "Send character when device is ready"
                            }
                        }) {
                            Text(text = "Prepare Device For DIM")
                        }
                        Button(onClick = {
                            handleTag() {
                                it.sendCharacter(dummyCharacter)
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

    private fun showWirelessSettings() {
        Toast.makeText(this, "NFC must be enabled", Toast.LENGTH_SHORT).show()
        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    }

    private fun buildOnReadTag(handlerFunc: (VBNfcHandler)->String): (Tag)->Unit {
        return { tag->
            val nfcData = NfcA.get(tag)
            if (nfcData == null) {
                runOnUiThread {
                    Toast.makeText(this, "Tag detected is not VB", Toast.LENGTH_SHORT).show()
                }
            }
            nfcData.connect()
            nfcData.use {
                val handler = vbNfcHandlerFactory.getHandler(nfcData)
                val successText = handlerFunc(handler)
                runOnUiThread {
                    Toast.makeText(this, successText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleTag(handlerFunc: (VBNfcHandler)->String) {
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