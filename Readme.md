#Integration

**This article will guide you through the process of Wigzo Android SDK integration.**
**You can integrate WigzoSDK in your app in simple steps.**

####Minimum Android Version:

> minimum of API Level 21.

####Add Wigzo SDK to your project
>Add dependency in build.gradle (app level):

>```implementation '<ADD_REPOSITORY PATH>'```

#Initializing Wigzo SDK
- Create a class and extend it with WigzoApplication.
- In onCreate() method, call super.onCreate() method.
- Initialize Wigzo SDK.

######example:
```java
package com.example.wigzosdkpocapp;

import com.wigzo.sdk.Wigzo;
import com.wigzo.sdk.base.WigzoApplication;

public class MyExtendedApplication extends WigzoApplication {

    public void onCreate() {
        super.onCreate(); //Always call this method first before whriting any other logic.
        Wigzo.initialize("<YOUR_ORGANIZATION_TOKEN>", getApplicationContext());
    }
}
```

####Update manifest file
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="....>
<application
        android:name=".MyExtendedApplication"
                ...
>
<activity android:name= ......></activity>
</application>
</manifest>
```

Congratulations! You have successfully integrated Wigzo SDK in your app. Let us guide you through usage of this SDK.

#Using Wigzo SDK

###Setting up Events (or Activity) tracking.

Below API's capture event occurred for a user. You can use these API's whenever an event or activity occurs.

####EventMapper
**To create event with only event name and value:**
>Create instance of EventMapper.
>Create an instance of EventData.
>Set eventName <String> and eventValue <String>.
>Add EventData instance to EventMapper.

####EventData
**EventData includes the following information:**

| fields | Description |
|:---------------------|:----------------------------------------------|
| eventName | name of the event |
| eventValue | value of the event |
| eventMetaData | additional information about event |

####Metadata
**To create event with additional event information :**
> 1. Create instance of EventMetaData.
> 2. Set this instance in EventData instance

**Metadata includes information :**

| fields | Description |
|:------------|:--------------------------|
| productId   | Product Id of Item |
| title | Title of item |
| description | Description of item |
| url | Web or android url of Item |
| tags | Tags related to item |
| price | Price of Item |

####DeviceInfo
***To create event with device information :***
> 1. To add DeviceInfo, create instance of DeviceInfo.
> 2. Set this instance in EventMapper instance.

**DeviceInfo includes information :**

| fields | Description |
|:--------------|:--------------------------|
| device | Build of the device |
| os | Android |
| osVersion | version of OS |
| ipAddress | IP address of the device |
| appVersion | Your app version |
| location | Location of the device |

> By default, device, os, osVersion, ipAddress and appVersion is prefilled. However, you still can set your own values by using their respective setters.

####Location
**To add location in DeviceInfo :**
> 1. Create an instance of Location
> 2. Set it to DeviceInfo instance.

**Location includes information:**

| fields        | Description |
|:--------------|:----------------------------------------------|
| countryCode | Country code |
| stateName | name of the state in which Device is located |
| stateCode | code of the state in which Device is located |
| city | name of the city in which Device is located |

>NOTE: Event name and Event value both are required to create an event.

Example:
```java
// Required:
EventData eventData = new EventData();
        eventData.setEventName("search");
        eventData.setEventValue(searchText.getText().toString());

// OPTIONAL:
        EventMetaData eventMetaData = new EventMetaData();
        eventMetaData.setProductId("<ProductId>");
        eventMetaData.setPrice("40");
        eventMetaData.setDescription("Product description");
        eventMetaData.setTitle("Product Name/Title");
        eventMetaData.setUrl("https://yourwebsite.com/path/to/the/product");
        eventData.setMetadata(eventMetaData);

// OPTIONAL:
        Location location = new Location();
        location.setCountryCode("Country code");
        location.setStateName("Name of state");
        location.setStateCode("State code");
        location.setCity("Name of the city");

// OPTIONAL:
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDevice("Device name");
        deviceInfo.setOs("Device OS");
        deviceInfo.setOsVersion("OS VERSION");
        deviceInfo.setIpAddress("Device IP Address");
        deviceInfo.setAppVersion("Your App Version");
        deviceInfo.setLocation(location);

        EventMapper eventMapper = new EventMapper();
        eventMapper.addEventData(eventData);
        eventMapper.setDeviceInfo(deviceInfo);
        eventMapper.push();
```

---
###Setting up User Profile tracking.
Below API's help you capture profile of User. User’s profile should be created only once.
####UserProfileMapper
> Wigzo has provided a class “UserProfileMapper” which can be used to capture user's profile.

**To create User profile:**
> 1. Create an instance of UserProfileMapper.

**UserProfileMapper ​class includes following fields :**

|  Fields | Description |
|:----------------|:---------------------------------|
| fullName | Full name of user |
| userName | User name of user |
| email | Email of user |
| organization | Organization of the user |
| phone | Phone number of user |
| gender | Gender of user |
| birthYear | Birth year of user |
| customData | Json to provide any other data about user |

> 1. Overloaded Constructors of this class can be called as mentioned below:
> 2. Setters are  also provided for other fields.

**To save and send User profile data to Wigzo , following method can be used :**
> `<userProfile_instance>.push();`

####Exceptional Case :
####EmailMapper
> When User profile information is not there but only email of user is there,
in such a cases, create an instance of EmailMapper class.

```java
UserEmailMapper userEmailMapper = new UserEmailMapper(<email>);
        userEmailMapper.push();
```

> Setter is also provided to set email explicitly.

>***Note***: Please note that in this case user profile is not getting created and only email id is stored for tracking purposes.

###FIREBASE

#####To integrate Firebase in your App [Click here](https://firebase.google.com/docs/cloud-messaging)

There are few events which are required to be sent to Wigzo in order to show you the correct information about the campaigns triggered from Wigzo Dashboard.

First you need to map FCM token with Wigzo and then register the token.

####Map FCM
To map FCM with Wigzo, create an instance of FCMMapper class with the FCM token and push it. Wigzo is required to be notiofied about the token every time the token is generated.

####Register FCM
To Register FCM with Wigzo, create an instance of FCMRegister class with the FCM token and push it. This step also is mandatory whenever a new token is generated.

>***Note***: Map FCM and Register FCM both are required when a new token is generated. Also Map FCM should happen before registering.

Example:
In your FCM Listener service
```java
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        FCMMapper fcmMapper = new FCMMapper(token);
        fcmMapper.push();

        FCMRegister fcmRegister = new FCMRegister(token);
        fcmRegister.push();
    }
```

####Notifying Wigzo about the notification received:
Notifying Wigzo that the notification has been recieived by the user is important, as it helps us keep track of the data which has been sent by Wigzo to provide you with the important insights about your campaigns. To do so:

- In the `onMessageReceived()` method in your FCM Listener class, crate an instance of NotificationRecieved class. It requires Organization ID and Campaign ID which came along the notification, sent via Wigzo dashboard.
  Example:
```java
@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                /*scheduleJob();*/

                long orgId = Long.parseLong(remoteMessage.getData().get("organizationId"));
                long campaignId = Long.parseLong(remoteMessage.getData().get("id"));

                NotificationRecieved notificationRecieved = new NotificationRecieved(orgId, campaignId);

                // Send notification to the Notification Bar
                sendNotification(remoteMessage.getData().get("body"), campaignId, orgId);
                notificationRecieved.push();
            } else {
                ....
            }

        }
        .....
        }
    }
```

####Notifying Wigzo about the notification Clicked:
Notifying Wigzo about the notification has been clicked by the user is another crucial step as it helps Wigzo collecting the data about the campaigns triggered from the Wigzo Dashboard. To do so:

- While you are sending the notification to the Notification Manager, create an Intent which contains a target Activity (which will be opened when the notificaiton is clicked)
- Put Bundle extras which contains the `campaignId` and the `organizationId`(you'll get the campaignId and OrganizationId in the data received from the notification sent from the Wigzo Dashboard).
- Create a Pending Intent and provide the Intent created above to it.
- Attach the Pending Intent created above to the notificaiton builder.
- Build the Notifiation.

Now when the user clicks on the notification, the target activity set through the intent while creating the notification is opened.

When the Target Activity is opened, extract the Bundle extras that were sent from the Notification Builder. Extract the `campaignId` and the `organizationId`. Create an instance of NotificationOpen class and pass the fetched `campaignId` as well as the `organizationId`, and push the instance in the queue.

Example:

**In your `sendNotifiation()` method,**
```java
private void sendNotification(String messageBody, long campaignId, long organizationId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("campaignId", campaignId);
        intent.putExtra("organizationId", organizationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = "0";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Title")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
```

**When the target activityis opened, inside the `onCreate()` method,**
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ...
        ...
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            long campaignId = extras.getLong("campaignId");
            long organizationId = extras.getLong("organizationId");

            Log.d("campaignId is ", "" + campaignId);
            Log.d("organizationId is ", "" + organizationId);

            NotificationOpen notificationOpen = new NotificationOpen(campaignId, organizationId);
            notificationOpen.push();
        }
        ...
        ...
    }
```

>The above code will create the Notification with the `campaignId` and the `organizationId` packed within the notificaiton.

***To save all the Events use the `.push()` method of the instance of that particular event.

###In-App Notifications

In-App Notifications are the pop-ups (Dialogs) which are required to be displayed when a notification (of push type : inapp or both) is received.

#####An In-App notification dialog consists of the following components:
- Image section (If template supports one).
- Title section (notification title).
- Body section (notification message/body).
- Positive response button (Ok button).
- Negative response button (Cancel or X button).

When the negative response button is pressed it simply closes the dialog and no action is taken.

When the "Ok" button is pressed it will take the user to an "activity" (another screen) along with the notification data (notification title, body, image url, campaign id, organization id, intent data (payload) , etc).
Now in that activity your logic to take further action should be written, like redirecting user to some another screen and notifying wigzo about the notification click (mentioned above in this document).

Redirection can be based on the key-value pairs received in the intent data (payload). It is recommended that you use deep links to redirect users to another screen. If deep links are not possible use appropriate key-value pairs (sent via Wigzo Dashboard) for unique identification and redirection.

####Integrate In App Notifications

To integrate In App Notifications, the very first step is to extend all your activities with WigzoActivity.
WigzoActivity extends ```androidx.appcompat.app.AppCompatActivity```. This is necessary because, in order to display any Dialog we need to have the Context of the running activity, so that it can be run on the main UI thread. Your code should look like this:

 ```java
 //
 ...
import com.wigzo.sdk.base.WigzoActivity;
....
//

public class MainActivity extends WigzoActivity {
	super.onCreate(savedInstanceState);
	...
}
 ```

***Note***: If (for some reason) you cannot extend one or all of your activities, then at the beginning of the ```onCreate()``` method, just after calling ```super.onCreate()``` method, set the context of your current activity in WigzoApplication as mentioned below:
```
//
...
import com.wigzo.sdk.base.WigzoApplication;
...
//
public class <YOUR_ACTIVITY_NAME> extends AppCompatActivity {
		super.onCreate(savedInstanceState);
        WigzoApplication.setAppContext(this);
		...
		//
}
```

####Displaying In App Notifications
When a notification is sent from the Wigzo Dashboard, it contains a key name ```type```.  You can display the In App notifcation based upon its value. Possible values are ```push```, ```inapp```, ```both```.

To create an In App Notification, when a notification is received, in the ```MyFirebaseMessagingService``` class (created by you), inside the onMessageReceived method create an instance of ```InAppMessageHandler``` class and call ```createNotification()``` method. ```InAppMessageHandler ``` constructor takes the following agruments,
- remoteMessageData (data recieved in the remote message).
- positive response target activity (Activity which is required to be opened when the positive response (Ok) button is clicked).
- negative response target activity (Activity which is required to be opened when the negative response (Cancel or X) button is clicked).

>InAppMessageHandler has the following setters:
```java
setImageUrl(String imageUrl)
setTitle(String title)
setBody(String body)
setPayload(JSONObject payload)
setRemotePicture(Bitmap remotePicture)
setCanceledOnTouchOutside(boolean canceledOnTouchOutside) // Default is true.
setOnDismissContext(Context context) // Required
setOnCancelContext(Context context) // Required
setPositiveResponseTargetActivity(Class<? extends AppCompatActivity> positiveResponseTargetActivity) // Required
setNegativeResponseTargetActivity(Class<? extends AppCompatActivity> negativeResponseTargetActivity) // Required
setLayoutID(String layoutId)
```

>#####setImageUrl(String imageUrl)
sets the image url which will be displayed in the In App Notification Dialog.
***returns***:  instance of InAppMessageHandler.

>#####setTitle(String title)
Sets the title of the notification which will be displayed in the In App Notification Dialog.
***returns***:  instance of InAppMessageHandler.

>#####setBody(String body)
Sets the body/message of the notification which will be displayed in the In App Notification Dialog.
***returns***:  instance of InAppMessageHandler.

>#####setPayload(JSONObject payload)
Sets the payload if the notification.
***returns***:  instance of InAppMessageHandler.

>#####setRemotePicture(Bitmap remotePicture)
Sets the image of the notification which will be displayed in the In App Notification Dialog.
***returns***:  instance of InAppMessageHandler.

>#####setCanceledOnTouchOutside(boolean canceledOnTouchOutside)
Set whether the notification is supposed to be cancelled if toushed outside of the dialiog.
***returns***:  instance of InAppMessageHandler.

>#####setOnDismissContext(Context context)
Required
Set the context of the class which implements the InAppMessageHandler.OnDismissListener.
***returns***:  instance of InAppMessageHandler.

>#####setOnCancelContext(Context context)
Required
Set the context of the class which implements the InAppMessageHandler.OnCancelListener.
***returns***:  instance of InAppMessageHandler.

>#####setPositiveResponseTargetActivity(Class<? extends AppCompatActivity> positiveResponseTargetActivity)
Required
Activity to be redirected to when the user clicks on the positive response of the dialog.
***returns***:  instance of InAppMessageHandler.

>#####setNegativeResponseTargetActivity(Class<? extends AppCompatActivity> negativeResponseTargetActivity)
Required
Activity to be redirected to when the user clicks on the negative response of the dialog.
***returns***:  instance of InAppMessageHandler.

>#####setLayoutID(String layoutId)
Sets the layout ID of the dialog.
***returns***:  instance of InAppMessageHandler.


Example:

```java
public class MyFirebaseMessagingService extends FirebaseMessagingService implements InAppMessageHandler.OnDismissListener , InAppMessageHandler.OnCancelListener {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

		// Check if message contains a data payload.
        Map<String, String> remoteMessageData = remoteMessage.getData();
        if (remoteMessageData.size() > 0) {
		// Notifying Wigzo that the notification has is received.
		NotificationRecieved notificationRecieved = new NotificationRecieved(orgId, campaignId);
                notificationRecieved.push();

			InAppMessageHandler inAppNotificationHandler = new InAppMessageHandler(remoteMessageData, PositiveResponseActivity.class, NegativeResponseActivity.class);

			// To explicitly set the value of any instance variable, we have have provided their setters, as shown below.

			inAppNotificationHandler
			.setImageUrl("<SOME_URL>")
			.setTitle("<IN_APP_NOTIFICATION_TITLE>")
			.setBody("<IN_APP_NOTIFICATION_BODY>")
			.setPayload(<EXTRA_PAYLOAD>) // Replace the payload from the notification with your own payload.
			.setRemotePicture(<REMOTE_PICTURE_BITMAP>) // Use this if you want to display some other image instead of the image_url sent from the notification.
			.setPositiveResponseTargetActivity(<SOMEACTIVITY.CLASS>)
			.setNegativeResponseTargetActivity(<SOMEACTIVITY.CLASS>)
			.setLayoutID("<LAYOUT_ID>") //use this method only if you know which layout id you want to use explicitly.
			.setCanceledOnTouchOutside(<BOOLEAN_VALUE>) // set false if you do not want the dialog to be cancelled on outside touch by the user.
			.setOnDismissContext(this) // Context of the class which implements InAppMessageHandler.OnDIsmissListener interface
			.setOnCancelContext(this) // Context of the class which implements InAppMessageHandler.OnCancelListener interface

			inAppNotificationHandler.createNotification();
        }
    }

    @Override
    public void onNewToken(String token) {
        ...
    }

	@Override
    public void onDismiss(Map<String, String> remoteMessageData) {
        // YOUR CODE
    }

    @Override
    public void onCancel(Map<String, String> remoteMessageData) {
        // YOUR CODE
    }
}
```

> Note: ```onCancel``` is called every time the dialog is closed/cancelled. ```onDismiss``` is called every time the dialog is dismissed/cancelled/closed.

We internally use ```dismiss() ``` method whenever a ```positiveResponse``` or a ```negativeResponse``` button is pressed by the user. If the dialog is cancelled by clicking outside the dialog or by performing the ```back``` action, ```onCancel()``` and ```onDismiss()``` both methods are invoked.

When the dialog is cancelled, the user will not be taken to the negative response target activity, so to handle such case, it is recommended to use implement ```InAppMEssageHandler.OnCancelListener```

####Notifying Wigzo that some action has been taken.
When a user clicks either "Ok" or "Cancel" button, a new activity is opened (if provided by you). There you can notify Wigzo that the notification has been clicked with a positive response or a negative response. In that activity you can write your own logic to further take them to some other Screen (Activity) based upon the payload or notification data received.

```java
	NotificationOpen notificationOpen = new NotificationOpen(campaignId, organizationId);
	notificationOpen.push();

	// campaignId and organizationId can be retrieved from the notification data.
```

> ```notificationData``` is passed as Bundle extra. You can use it to further take actions in the ```targetActivity``` passed by you. It contains all the notification data received in the notification (if passed by you).

> ***Note***:This implementation is same as the notidfication clicked as mentioned earlier in this documentation, when a user clicks on the notification in the notification drawer and an activity is opened.


>It is highly recommended to provide ```positiveResponseTargetActivity``` and ```negativeResponseTargetActivity```for proper usage of the SDK and ease of implementation on notification clicked or cancelled.
>It is also highly recommended to use high resolution images (at least 1024px in width).
