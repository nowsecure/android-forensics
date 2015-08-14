android-forensics
=================

AFLogical OSE: Open source Android Forensics app and framework

The Open Source Edition has been released for use by non-law enforcement personnel, Android aficionados, and forensics gurus alike. It allows an examiner to extract CallLog Calls, Contacts Phones, MMS messages, MMSParts, and SMS messages from Android devices. The full AFLogical software is available free for Law Enforcement personnel. More information is available at https://www.nowsecure.com/

Installing
----------
Compile or [download](https://github.com/viaforensics/android-forensics/downloads) the latest apk. Alternativley, AFLogical OSE comes pre-installed in [santoku linux](https://santoku-linux.com/)

Then Install the apk file to your device. Either copy the apk to your device and run it on the device OR Use adb shell.

Example;
  `adb install AFLogical-OSE_1.5.2.apk`

Usage
-----

On your Android device, open the AFLogical OSE application, choose what data you want to extract, and follow the prompts to extract the data.
Note: You must have an SD card installed on your device (or a built in SD card) to extract the data.

The selected data is then extracted to your SD card (external or internal).

You can then copy the data from your SD card to your computer to view the content, either by removing the external SD Card and connecting it to your computer, or using adb pull.

Example;
```
adb pull /sdcard/forensics/ ~/Desktop/AFLogical_Phone_Data
pull: building file list...
pull: /sdcard/forensics/20120720.1833/Contacts Phones.csv -> /home/santoku-user/Desktop/AFLogical_Phone_Data/20120720.1833/Contacts Phones.csv
...< snip >...
40 files pulled. 0 files skipped.
410 KB/s (3880025 bytes in 9.229s)
```

Your extracted data is in your ~/Desktop/AFLogical_Phone_Data directory.

Contributing
------------
If you would like to contribute code, please fork this repository, make your
changes, and then submit a pull-request.
