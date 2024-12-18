package com.github.cfogrady.vbnfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.provider.Settings
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

class MainActivity : ComponentActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private var vbNfcHandlerFactory: VBNfcHandlerFactory
    private var secrets: VBNfcHandler.Secrets

    init {
        secrets = VBNfcHandler.Secrets("", "", intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)) // Not Real Keys or cypher.
        vbNfcHandlerFactory = VBNfcHandlerFactory(secrets, secrets, secrets)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maybeNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (maybeNfcAdapter == null) {
            Toast.makeText(this, "No NFC on device!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        nfcAdapter = maybeNfcAdapter
        setContent {
            var secret1 by remember { mutableStateOf(secrets.secretKey1) }
            var secret2 by remember { mutableStateOf(secrets.secretKey2) }
            LibVbNfcExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row {
                            Text(text = "Secret Key 1")
                            TextField(value = secret1, onValueChange = {
                                secret1 = it
                            })
                        }
                        Row {
                            Text(text = "Secret Key 2")
                            TextField(value = secret2, onValueChange = {
                                secret2 = it
                            })
                        }
                        Button(onClick = {
                            secrets = VBNfcHandler.Secrets(secret1, secret2, secrets.substitutionCypher)
                            vbNfcHandlerFactory = VBNfcHandlerFactory(secrets, secrets, secrets)
                        }) {
                            Text(text = "Regenerate NFC Handler From Keys")
                        }
                        Button(onClick = {
                            readCharacter()
                        }) {
                            Text(text = "Read Character")
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

    private fun onReadCharacterTag(tag: Tag?) {
        val nfcData = NfcA.get(tag)
        if( nfcData == null) {
            Toast.makeText(this, "Tag detected is not VB", Toast.LENGTH_SHORT).show()
        }
        nfcData.connect()
        nfcData.use {
            val handler = vbNfcHandlerFactory.getHandler(nfcData)
            handler.receiveCharacter()
            Toast.makeText(this, "Done Reading Character", Toast.LENGTH_SHORT).show()
        }
    }

    fun readCharacter() {
        if (!nfcAdapter.isEnabled) {
            showWirelessSettings()
        } else {
            val options = Bundle()
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
            nfcAdapter.enableReaderMode(this, this::onReadCharacterTag, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                Bundle()
            )
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