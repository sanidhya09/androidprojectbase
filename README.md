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
1. For Location Services :
    
```
        // To start Location Services
        final LocationUtil locationUtil = LocationUtil.getInstatnce();
                locationUtil.init(this);
        
                LocationUtil.LocationConfig locationConfig = new LocationUtil.LocationConfig();
                locationConfig.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationUtil.setConfig(locationConfig).startLocationUpdates();
        
                locationUtil.setOnLocationChangeInterface(new LocationUtil.GetLocationUpdates() {
                    @Override
                    public void getLocation(Location location) {
                        
                    }
                });

        // To Stop Location Services
        locationUtil.stopLocationUpdates();
        
        // To get the human readable address
        List<Address> address = locationUtil.getAddress(location.getLatitude(), location.getLongitude())
```

2. For RestServices RetroFit2 :

```
   // initializes retrofit service
      RetroFitUtil retroFitUtil = new RetroFitUtil("Your base URL Here ");
  
   // create your own RestService class
     RestService restService = retroFitUtil.getRetrofit().create(RestService.class);
```

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

