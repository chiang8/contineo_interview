=== Introduction ===
This is a simple web application for managing Inventory. It is built upon Springboot + Spring MVC. For simplicity sake, there is NO proper datastore like database. It is just backed by a Map.
THere is no front-end for this app. It solely provides a REST API and accepts/returns json as the content type.

IDE: Eclipse 2020-03
JDK: AdoptOpenJDK 11.0.9
Dependency management: Gradle


=== How to start ===
Please start up the webapp by running com.contineo.inventory.ContineoInventoryApplication. 
You may run "/ContineoInventoryApplication.launch" if you are using Eclipse.


=== How to test ===
For integration testing, you may use http://localhost:8081/contineo_inventory/swagger-ui.html.
I could have written test harnesses to cover this part but I didn't due to time constraint.

For unit test, you may run the script at "/Unit test - contineo_interview.launch"
Not all of the classes are covered with unit test, as I am running out of time. 
Please check out InventoryControllerTest, which contains the most important unit test.


=== API === 
Please start up the application and check out the API. There is also a static version at root folder(/api-docs.yaml).
swagger-ui: http://localhost:8081/contineo_inventory/swagger-ui.html
yaml: http://localhost:8081/contineo_inventory/v3/api-docs.yaml


=== Category and sub-category mapping ===
There is a mapping for category and sub-category which is stored under "/src/main/resources/category.properties".
