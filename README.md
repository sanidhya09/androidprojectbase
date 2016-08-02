Android Project Base
====


What's this?
----

It is a ready to start development for an Android app project sample with base library. 

It includes:

1. Retrofit 2.0.2 with sample implementation for your rest calls.
2. Dagger 2 based Dependency Injection with sample UtilitySingleton implementation.
3. RX java sample implementation.
4. Toolbar and Navigation drawer with RecyclerView.
5. Custom fonts class with its implementation in XML file.
6. LocationUtil based on fused location for getting current locations.
7. GCM based push notification based on play services 8.4
8. Marshmallow permission request sample.
9. Google AutoComplete Places Widget for searching places.

How to use
----

1. Clone or download this project.
2. Import this project in Android Studio.
3. Rename the packages as per your need.
4. Use this project for your application development.

Usage
----
##### 1. For Location Services :

### Starting

For starting the location service single time:

````java

LocationUtil.with(context).location().oneFix().start(new OnLocationUpdatedListener() {
@Override
public void getLocation(Location location) {
                   
}
});
````

For starting the location service (Periodic):

````java
LocationConfig locationConfig = new LocationConfig();
locationConfig.setInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

LocationUtil.with(context).location().setConfig(locationConfig).start(new OnLocationUpdatedListener() {
@Override
public void getLocation(Location location) {
                   
}
});
````

### Stopping

For stopping the location just use the stop method.

````java
LocationUtil.with(context).location().stop();
````
      
                
````java      
// To get the human readable address
List<Address> address = LocationUtil.with(context).location().state().getAddress(location.getLatitude(), location.getLongitude());
        
// Check if the location services are enabled
LocationUtil.with(context).location().state().locationServicesEnabled();
        
// Check if any provider (network or gps) is enabled
LocationUtil.with(context).location().state().isAnyProviderAvailable();
        
// Check if GPS is available
LocationUtil.with(context).location().state().isGpsAvailable();
        
// Check if Network is available
LocationUtil.with(context).location().state().isNetworkAvailable();
        
// Check if the passive provider is available
LocationUtil.with(context).location().state().isPassiveAvailable();
        
// Check if the location is mocked
LocationUtil.with(context).location().state().isMockSettingEnabled();
````

##### 2. Geofencing :

 GeofenceUtil helps in monitoring the geofences. we can define single as well as multiple geofences to monitor.

### Starting

For Single Geofence

````java
   GeofenceModel janakpuri = new GeofenceModel.Builder("id_janakpuri") // this id should be unique for every GeofenceModel
                .setTransition(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLatitude(28.621127)
                .setLongitude(77.081824)
                .setRadius(2000)
                .setExpiration(6000000)
                .build();
                
        GeoFenceUtil.INSTANCE.add(janakpuri).init(this).setOnGeoFenceStatusListener(new OnGeofenceStatusListener() {
            @Override
            public void onStatus(String id, int transitionType, Location location) {
                Log.i(TAG, id + " " + transitionType);
            }
        });
````

For Multiple Geofence

````java
        GeoFenceUtil.INSTANCE.add(address1).add(address2).init(this).setOnGeoFenceStatusListener(new OnGeofenceStatusListener() {
                    @Override
                    public void onStatus(String id, int transitionType, Location location) {
                        Log.i(TAG, id + " " + transitionType);
                    }
                });
````

### Stopping

````java
GeoFenceUtil.INSTANCE.removeGeofence();
````

##### 3. For RestServices using RetroFit 2 :


````java
// initializes retrofit service in application class or via dependency injection (To be initialized once)

              Headers.Builder builder = new Headers.Builder();
              builder.add("OS", "ANDROID");
      
              RetrofitConfigModel retrofitConfigModel = new RetrofitConfigModel.Builder()
                      .setBaseUrl(API_URL)
                      .setConnectOutTime(60)
                      .setReadOutTime(45)
                      .setLoggingEnabled(true)
                      .setHeaders(builder)
                      .build();
      
              RetroFitSingleton.INSTANCE.setRetrofitConfig(retrofitConfigModel);
  
     // create your own RestService class
     RestService restService = RetroFitSingleton.INSTANCE.getRetrofit().create(RestService.class);
     
     // download large files
     retroFitUtil.downloadLargeFile("https://pubs.usgs.gov/dds/dds-057/ReadMe.pdf", "file name", "file extension", this);
     
     // Multiple Image upload request generator
     // Pass this requestMap to @PartMap in rest service interface.
     Map<String, RequestBody> requestMap = uploadImagesRequestGenerator(fileArray, file extension, keyParam);
````

##### 4. UtilityClass for rapid development:

1. Check internet connection
````java
isOnline();
````

2. Uncompress zip file
````java
decompressZipFile(appname, fileName, password);
````

3. Validate email
````java
validateEmail(edittext);
````

4. Get String path from URI
````java
    getPath(uri, context);
````

5. Hide Soft Keyboard
````java
hideSoftKeyboard(view)
````

LICENSE
----

```
Copyright 2016 Sanidhya Kumar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

