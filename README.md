# lib-vb-nfc
Android Library And Demo App for VB NFC Communication

This library aims to make NFC communication with the Vital Bracelet family of products accessible if users have the correct keys.

# Required Setup
This project uses secret keys and ciphers. The user/developer is responsible for identifying those
secrets. They are collected in single locations in this project. The files discussed here are in
`.gitignore` so as to not be accidentally checked in.

## App
Create a `keys.xml` file inside `lib-vb-nfc/res/values/keys.xml` with the following:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="beKey1"></string>
    <string name="beKey2"></string>
    <string name="vbdmKey1"></string>
    <string name="vbdmKey2"></string>
    <string name="vbcKey1"></string>
    <string name="vbcKey2"></string>
    <string name="aesKey"></string>
    <integer-array name="beSubstitutionArray">
        <item>0</item>
        <item>1</item>
        <item>2</item>
        <item>3</item>
        <item>4</item>
        <item>5</item>
        <item>6</item>
        <item>7</item>
        <item>8</item>
        <item>9</item>
        <item>10</item>
        <item>11</item>
        <item>12</item>
        <item>13</item>
        <item>14</item>
        <item>15</item>
    </integer-array>
    <integer-array name="vbSubstitutionArray">
        <item>0</item>
        <item>1</item>
        <item>2</item>
        <item>3</item>
        <item>4</item>
        <item>5</item>
        <item>6</item>
        <item>7</item>
        <item>8</item>
        <item>9</item>
        <item>10</item>
        <item>11</item>
        <item>12</item>
        <item>13</item>
        <item>14</item>
        <item>15</item>
    </integer-array>
</resources>
```
The values should be replaced by correct secrets on your local file.

# Composition
## app
This is a simple app demonstrating how the library can be used.

## vb-nfc-reader
This is the Android library responsible for reading and writing data to the Vital Bracelet device over NFC.

# Legal
Preface: I am not a lawyer, this is just based on my interpretation of local laws to show that everything is done in good faith. If you believe I am infringing on any of your rights please open an issue and I will seek to rectify it as soon as possible.

I am in no way affiliated with Bandai nor anyone else who own any trademarks that may appear here. Any display of trademarks here is purely to indicate the purpose of this project and in accordance with good business practice, as permitted by relevant laws.

There are no copyrighted works distributed here, nor works derived from copyrighted works, except works with a free and open source license permitting distribution. I have no incentive or intention to do anything that would infringe on anyone's copyrights, trademarks or other rights.
