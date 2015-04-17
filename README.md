# taxis-backend
API RESTful para gerenciamento de táxis utilizando Spring MVC 4.0 e MongoDB

# API Doc

##Driver Status 
##Responsible to keep if the position of a driver and if he is available or not.

Request: GET /drivers/status?driverId=1<br/>
Success Response: {"statusHTTP":200,"success":true,"message":"","object":{"driverId":4,"available":true,"latitude":-56.789,"longitude":110.9876}}<br/>
Error Response: {"statusHTTP":500,"success":false,"message":"Couldnt find driver for driverId (10)"}<br/>
Description: Returns a driver status data.

Request POST /drivers/status?driverId=1&latitude=10.8908&longitude=-170.987&available=true<br/>
Success Response Example: {"statusHTTP":200,"success":true,"message":""}<br/>
Error Response Example: {"statusHTTP":400,"success":false,"message":"Invalid latitude/longitude data. Must be between -180 and 180"}<br/>
Description: Upd<br/>ates if driverId exists or inserts a drive status if it doesn't. All parameters are mandatory.

Request GET /drivers/inArea?sw=-10.8989,8.98098&ne=100.989,64.98283&availableOnly=true<br/>
Success Response Example: {"statusHTTP":200,"success":true,"message":"","object":[{"driverId":4,"available":true,"latitude":-56.789,"longitude":110.9876},{"driverId":5,"available":false,"latitude":10.987654,"longitude":-10.909087},{"driverId":6,"available":true,"latitude":-120.98283,"longitude":80.9877}]}<br/>
Description: Returns all driver status in a rectangle set by the points given. Set availableOnly to true if you wish only available drivers to be returned (optional)

Request GET /drivers/inRect?center=-10.8989,8.98098&area=100&availableOnly=true<br/>
Response Example: See GET inArea <br/>
Description: Returns all driver status in a rectangle with center and area given in get parameters. Can also be used with availableOnly

Request GET /drivers/nearest?latitude=12.3&longitude=14.6&availableOnly=true<br/>
Response Success Example: See GET drivers/status<br/>
Response Error Example: {"statusHTTP":200,"success":false,"message":"No drivers found around this position. 1000.0 degrees^2 was searched."} <br/>
Description: search for the nearest driver (available if flag availableOnly is true). Maximum area search: 1000 degrees^2. The search area grows in a geometric progression as it doesnt find drivers.

##Driver 
##manage drivers and may insert or update their names and car plates

Request POST /drivers?carPlate=LAT-0000&name=Nameless<br/>
Response: {"statusHTTP":200,"success":true,"message":""}

Request GET /drivers?driverId=1<br/>
Response: {"statusHTTP":200,"success":true,"message":"","object":{"driverId":4,"name":"Luiz Felipe","carPlate":"AAA-9999"}}


