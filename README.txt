Author: Wen Zhou
06/21/2016


[Class Designs]:

-model package: contains the 4 models(User,Photo,Follow,Like) that are used for the Instagram app, and a Music model that is used for music player.
-database package: contains my database schema, open helper, cursor wrapper and DAO.
-image package: contains two of my custom ImageView (Round and Square), and the PhotoUtils.
-MainActivity: used to log in or go to the sign-up page
-SignUpActivity: used to register new user.
-homepage package: contains every fragment and activity that is used in the user homepage.


[App Description]

This app is basically a simple “emulator” for the famous Instagram app. Users can share photos, follow other users, get followed by other users, like their photos. The only difference is that I save all the user information or photo information in the database of this single device instead of a server, so all the users need to use this device.
