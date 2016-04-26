# Intilery Android SDK

### Prerequisites

To build this library you will need the following:

* A Bintray account
* Android Studio or equlivant
* Java JDK 1.8 or above
* A GPG Key (with passphrase)

### Building and Deployment

#### 1. Properties File
You will need to create a file  called `'bintray.properties'` in the root directory. This is needed to support the Bintray plugin so the library can be exported to the jcenter repository. The `'bintray.properties'` file should look something like this:
```
#Bintray Properties File
bintray.user=BINTRAY_USER
bintray.apikey=BINTRAY_API_KEY
bintray.org=BINTRAY_ORGANISATION
```
_Note: The API Key is generated from the Bintray website under the_ `Account > Your Profile > Edit`

#### 2. Building the Library
The gradle library comes with a handy installation script which can build your library so it's ready to be deployed to jcenter. This script is executed with the following command:
```
$ gradle install
```

#### 3. Deployment
When the library is ready to be deployed you must make a couple of minor changes to the file `sdk/build.gradle` to update the version of library. In the `ext` block there is a value called `libraryVersion` (this must be unique) and should follow the [Apache version numbering convention](https://apr.apache.org/versioning.html) of MAJOR.MINOR.PATCH e.g. 1.0.0, 0.2.24 or 2.22.1 (this is not a decimal convention). In the `android>defaultConfig` block there is a value called `versionName`, this should be the same as the libraryVersion.
Once this is set you should be ready to build and deploy the library to the jcenter repository. This can be done simply by executing the following:
```
$ gradle bintrayUpload
```
Once executed this will now be available for use in the jcenter gradle repository which can be included with the following (assuming that jcenter is in the gradle's list of repositories):
```
compile 'com.intilery.android:sdk:0.1.1'
```

#### Resources

* [Create a GPG Key](https://www.gnupg.org/gph/en/manual/c14.html)
* [Bintray](https://bintray.com)
* [Android Studio](http://developer.android.com/tools/studio/index.html)