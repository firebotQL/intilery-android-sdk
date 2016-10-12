# Intilery Android SDK
## Preamble
The Intilery Android SDK allows you to easily interact with the Intilery API from within your application.
## Installation
At the moment the Intilery API is not yet available on the maven central repo. However you can still install it to a maven repository.
### Local Installation
- Clone this repo into a folder
- Run `gradle install` to install the API into your local repository
- Add the dependency to your `build.gradle` within the module you would like to use the library with.

## Usage
Before use, create an instance of the Intilery API.
```java
new Intilery(IntileryConfig.builder()
    .url("https://www.intilery-analytics.com/api") //Changes if you are on your own AWS cluster.
    .intileryToken("*********") //Token found in Intilery App
    .appName("zed-test1") //As found in the Intilery App
    .rootContext(someContext) // Context used to query android APIs
    .userAgent("Android App") //Optional
    .gcmToken(someRegistrationID) //Required for receiving push notifications
    .build());
    
```
### Sending an event
Event sending is achieved through an API
```java
Intilery.i().getIo().track(
    IntileryEvent.builder()
        .eventData(
            EventData.builder("Event Action", "Event Name") //Event name is optional.
                .data("ItemData", 
                    new MapBuilder<String, Object>()
                        .put("Name", "Red K Trainers")
                        .put("Price", "13.99")
                    .build()
                )
            .build()
        )
    .build()
);
```

### Managing User Details
To attach data to a visitor, e.g City, Language or any other property available within the Intilery Platform on your account - there is an API.
```java
    Intilery.i().getIo()
        .setVisitorProperties(
            PropertyUpdate.builder()
                .property("First Name", "Tom")
                .property("Last Name", "Jones")
                .property("Sex","Male")
            .build()
        );
```
Additionally if you want to get properties of a customer which Intilery is storing then you can use this API:
```java

    Intilery.i().getIo().getVisitorProperties(
        // We now declare the receiver once we get the response from the Intilery API
        new IntileryIO.PropertyReceiver() {
            @Override
            public void receive(Properties properties) {
                // You can use properties.get(PROPERTY_NAME) to get the received property
                System.out.println("The first name is: " + properties.get("First Name"));
            }
        }
        // Next we list the properties we want to fetch
        , "First Name", "Last Name");
```
### Managing the unique device ID
On first initialisation the API generates as a UUID. This UUID acts as a unique identifier for the device and is stored persistently.
Once created the API will contact Intilery servers to register with the platform as a device.
You can get the UUID we are using through:
```java
Intilery.i().getUserInfo().uuid();
```
If you ever want to reset this device ID then you can use:
```java
Intilery.i().getUserInfo().reset()
```

##  Push Events
The Intilery Platform supports sending Push Notifications as part of a campaign. To analyse how engaging your push notifications are you may want to send an event when someone acts upon a notification (e.g opening app). In the future a set of APIs will be available to deal with this however until then please contact your account manager to find out which events you need to send to allow them to integrate with the platform.
