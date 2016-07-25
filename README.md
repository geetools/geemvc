# Geemvc

Geemvc is a fast lightweight MVC-framework written for Java8+. All classes within Geemvc have been injected by Google-Guice, making it the most adaptable and flexible Java MVC-framwework. In order to simplify extending Geemvc we have ensured that no private or static fields and methods exist, therefore making it the No. 1 choice for framework developers that need a good starting point. In many cases however it is not necessary to extend and override classes with Google-Guice. Instead, you simply create your own adapters that are automatically loaded by Geemvc.

## Goals

* Make it very easy to map requests to controller handler methods.
* Simplify validation and enable the usage of javax.validation checks (JSR-303).
* Integrate the usage of JSR-311 annotations (@Path, @GET, @Produces ...) to simplify the creation of REST APIs.
* Make it fun and easy to create modern webapps.
* Allow the framework itself to be very simple to extend by implementing adapter classes or by directly overriding functionality via Google-Guice injection.
* Increase the flexibility by allowing routing and validation rules to be definded in either Javascript, Groovy or MVEL.
* 
## Motivation

