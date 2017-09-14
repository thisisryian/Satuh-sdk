# Satuh sdk
This is an Android project allowing to login with satuh.

# Usage
Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
   repositories {
    maven { url "https://jitpack.io" }
   }
}
```

Add dependencies :
```gradle
dependencies {
     compile 'com.github.pmberjaya:Satuh:0.0.2'
}
```


## Declare variable
```class

Satuh satuh = new Satuh(#your client id(ex:"3"),#your url(ex:"http://example.com"));
AsyncSatuhRunner mAsyncRunner;



```
## Process
```class
mAsyncRunner = new AsyncSatuhRunner(satuh);

satuh.authorize(this, new Satuh.DialogListener() {
            @Override
            public void onComplete(String values) {
                    ...
                      mAsyncRunner.request(new AsyncSatuhRunner.RequestListener() {
                        @Override
                        public void onComplete(String response, Object state) {
                            String json = response;
                            try {
            
                                JSONObject profile = new JSONObject(json);
            
                                int id = profile.getInt("id");
                                String name = profile.getString("name");
                                String email = profile.getString("email");
                                String satuh_email = profile.getString("satuh_email");
                                String location_code = profile.getString("location_code");
                                String phone = profile.getString("phone");
            
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                         ...
                            }
                
                            @Override
                            public void onIOException(IOException e, Object state) {
                
                            }
                
                            @Override
                            public void onFileNotFoundException(FileNotFoundException e, Object state) {
                
                            }
                
                            @Override
                            public void onMalformedURLException(MalformedURLException e, Object state) {
                
                            }
                
                            @Override
                            public void onSatuhError(SatuhError e, Object state) {
                
                            }
                        });
               
             
            }

            @Override
            public void onSatuhError(SatuhError e) {
              
            }

            @Override
            public void onError(DialogError e) {
               
            }

            @Override
            public void onCancel() {
             
            }
        });
```
## Logout
```class

  mAsyncRunner.logout(getApplicationContext());
  
```
