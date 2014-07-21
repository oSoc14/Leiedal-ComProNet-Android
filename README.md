Leiedal-ComProNet-Android
=========================

## About
This repo holds the android implementation of ComProNet.
ComProNet is a community protection network that allows people to help police and emergency services 
by confirming and describing what is going on. A short video that explains the original idea is available both in
[Dutch](https://www.youtube.com/watch?v=msueB-uI_sE “Dutch”) 
[English](https://www.youtube.com/watch?v=v5ZRUHQRUYQ “English”)
Note that this app is a proof of concept; it works without a working backend.

## Structure

There are 2 folders
* Aandacht : contains the source code.
* GCMSend : A simple java program that send a google cloud notifications

## Installation & Contribution
### Aandacht
To contribute or install, you'll need to create a res/values/api_keys.xml file with:
* a google maps api key named "google_maps_android_api_v2"
* a google cloud message api key named "app_senderId"

### GCMSend
For the GCMSend app you'll need to create a class named properties.java with following information:
```java
public class properties {
    public final static String userName = <SenderId> + "@gcm.googleapis.com";
    public final static String password = <API key>;
    public final static String regId = <regId of the device>;
}
```
The regId can be optained by looking at the logs; the following line should appear:
... osoc.leiedal.android.aandacht I/LoginActivity﹕ devide already registered id: <regId>