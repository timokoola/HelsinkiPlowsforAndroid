HelsinkiPlowsforAndroid
=======================

Helsinki Snowplows on Android. This app is built upon Helsinki stara Open API that can be used to fetch almost real-time information about snowplows in Helsinki area. See https://github.com/codeforeurope/aura/wiki/API for details of the API. 

App is available free in Google Play at:
https://play.google.com/store/apps/details?id=com.moarub.lumiaura

Disclaimer about plow information
=================================

Tämä pilottiaineisto on Staran GPS-paikkatietoa talvihoidon töistä Helsingissä.  Pilotissa on mukana nyt 90 ajoneuvon tiedot.

Material for this app has been made available by city of Helsinki service organisation Stara. At the moment GPS-coordinates are available for 90 vehicles.

Building
========

There are two dependencies:

Maps
----

- Google Play services Maps API, needed jar is included and app will build itself. For further tweaking see documentation at: 
http://developer.android.com/google/play-services/maps.html
- Maps require developer to register for a service key. I have placed mine in keys.xml with a key google_maps_key. That value is referenced in AndroidManifest.xml like this: 

    android:name="com.google.android.maps.v2.API_KEY"
    android:value="@string/google_maps_key"

- Register your own key at Google Play services console

RoboSpice
---------

RoboSpice is a modular android library that makes writing asynchronous network requests easy. Required JARs are included in the project, see RoboSpice GitHub for more information:
https://github.com/octo-online/robospice

License
=======

Source code is published under Apache License version 2.0.

Graphics incorporated with this app sadly are not freely distributable. Three of the icons for the UI are from a http://www.androidicons.com/ . Icons are nice and licensing criteria reasonable, so go fetch a license. 




