# Geemvc

Geemvc is a fast lightweight MVC-framework written for Java8+. All classes within Geemvc have been injected by Google-Guice, making it the most adaptable and flexible Java MVC-framework. In order to simplify extending Geemvc we have ensured that no private or static fields and methods exist, therefore making it the No. 1 choice for framework developers that need a good starting point. In many cases however it is not necessary to extend and override classes with Google-Guice. Instead, you simply create your own adapters that are automatically loaded by Geemvc.

## Goals

* Make it very easy to map requests to controller handler methods.
* Simplify validation and enable the usage of javax.validation checks (JSR-303).
* Integrate the usage of JSR-311 annotations (@Path, @GET, @Produces ...) to simplify the creation of REST APIs.
* Make it fun and easy to create modern webapps.
* Allow the framework itself to be very simple to extend by implementing adapter classes or by directly overriding functionality via Google-Guice injection.
* Increase the flexibility by allowing routing and validation rules to be defined in either Javascript, Groovy or MVEL.

## Motivation

There are some very good MVC-frameworks out there - so why create another? When testing various MVC-frameworks for a large and very flexible E-Commerce platform we noticed that, although most of them meet our needs to a certain degree, there was always something that the framework lacked in order to completely fullfill our goals. And although the main frameworks offer good extension points, like adapter classes etc, or to override certain classes by configuration, there always seemed to be some caveat like private or static fields and methods, which made it impossible to extend the functionlity as needed. After a year of creating workarounds in order to make the required changes we made the decision to create our own MVC-framework, which is fun and simple to use and enables you to change practically any class to your needs.

## Requirements

* Java8+.
* Java8+ compatible servlet container.

## Example Controller
Simply annotate your controller and handler method and Geemvc will automatically find it.

```java
@Controller
@Request("/hello")
public class HelloWorldController {
    
    @Request("world")
    public String helloWorld() {
        System.out.println("Well done! You have successfully called the /hello/world controller.");        
        
        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}
```

## Passing URI Parameters to your Controller
Geemvc will automatically parse specified parameters out of the request URI and make them available to the handler method.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Request("/world/{id}")
    public String helloWorld(@PathParam Long id) {
        System.out.println("Well done! You can now use the id path parameter in your code. The id is: " + id);

        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}
```

## Passing Query Parameters to your Controller
Of course the same works for query parameters. Simply specify them in your method signature. Additionally you can retrieve values from headers, cookies or the current session. Just use the respective annotations in your handler methods. Standard possibilities are:

| Parameter Annotation | Description |
| --- | --- |
| @PathParam | Parameter retrieved from the request URI. |
| @Param | Parameter passed in via the query string. |
| @Cookie | Value retrieved from a cookie. |
| @Session | Value retrieved from the current session. |
| @Header | Value retrieved from the request headers. |

You can easily create your own annotations that are automatically evaluated by Geemvc. Please refer to the wiki page for more information.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Request("/world/{id}")
    public String helloWorld(@PathParam Long id, @Param String myQueryParam) {
        System.out.println("Thanks, you sent the following query parameter: " + myQueryParam);

        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}    
```

## Passing Parameters to the View
Here we are getting some fictitious value from an injected service and passing it to the view - our JSP page. Notice that previously we simply returned a string informing Geemvc where we want to forward the request to. In the following example we have exchanged the string for a Geemvc "View" object. This allows you to bind values to the view that you will be able to access in your JSP page or templating engine.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    protected Service someService;

    @Inject
    protected HelloWorldController(Service someService) {
        this.someService = someService;
    }

    @Request("/world/{id}")
    public View helloWorld(@PathParam Long id, @Param String myQueryParam) {
        System.out.println("Cool, I am passing the parameter 'myViewParam' to the view!");

	// The Views class is simply a small helper that builds the view object for you.
        return Views.forward("/WEB-INF/jsp/hello-world.jsp")
                .bind("myViewParam", someService.getById(id));
    }
}
```

## Validating a Parameter
The simplest way of validating your parameters is to use the javax.validation annotations or two additional ones provided by Geemvc (@Required and @Check). Notice in the example below the @Required annotation and the onError attribute that we have passed to the @Request annotation. The latter tells Geemvc where to go incase of a validation error. @Required obviously means that the id parameter must not be empty. Note that we are talking of "none-empty". This is useful for strings which are usually never null as empty form-fields get sent to the controller as an empty string. If you simply need to to check for "not null", you can use the javax.validation annotation @NotNull.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    protected Service someService;

    @Inject
    protected HelloWorldController(Service someService) {
        this.someService = someService;
    }

    /**
     * Because we have specified the "onError" attribute in the @Request annotation, Geemvc will not enter this
     * handler-method when it detects validation errors. If you want to check for further errors or dynamically
     * forward the user to some view, do not use this. Make use of the "Bindings" object as shown in the example 
     * following this one.
     */
    @Request(path = "/world/{id}", onError="/WEB-INF/jsp/hello-world.jsp")
    public View helloWorld(@Required @PathParam Long id, @Param String myQueryParam) {
        System.out.println("Cool, I am passing the parameter 'myViewParam' to the view!");

        return Views.forward("/WEB-INF/jsp/hello-world.jsp")
                .bind("myViewParam", someService.getById(id));
    }
}
```

## Checking for Errors in your Handler Method
Geemvc automatically passes two objects into your handler method in order to let you check if any validation errors exist. For this, simply add the "Bindings" and/or "Errors" object(s) to your method signature.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    protected Service someService;

    @Inject
    protected HelloWorldController(Service someService) {
        this.someService = someService;
    }

    @Request(path = "/world/{id}")
    public View helloWorld(@Required @PathParam Long id, @Param String myQueryParam, Bindings bindings, Errors errors) {

        // Lets check if validation errors exist.
        if (bindings.hasErrors()) {
            // Optionally we will add another message to the validation errors.
            errors.add("Hey, I also want to add this error message!");
            
            // Now we tell Geemvc where to go in case of a validation error.
            return Views.forward("/WEB-INF/jsp/some-other.jsp")
                    .bind("myViewParam", someService.getById(id));
        }

        return Views.forward("/WEB-INF/jsp/hello-world.jsp")
                .bind("myViewParam", someService.getById(id));
    }
}
```

## Form Example

### The Controller

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    protected Service someService;

    @Inject
    protected HelloWorldController(Service someService) {
        this.someService = someService;
    }

    /**
     * Takes the user to the form page.
     */
    @Request("world-form")
    public String helloWorld() {
        return "forward: /WEB-INF/jsp/hello-world-form.jsp";
    }

    /**
     * Attempts to save the form. If a validation error occurs we go back to the form, otherwise we show the success page.
     */
    @Request(path = "save-world-form", method = HttpMethod.POST)
    public View saveWorld(@Valid WorldBean world, Bindings bindings, Errors errors) {

        // Lets check if validation errors exist.
        if (bindings.hasErrors()) {

            // Now we tell Geemvc where to go in case of a validation error.
            return Views.forward("/WEB-INF/jsp/hello-world-form.jsp")
                    .bind(bindings.typedValues()); // Re-bind values to view.
        }

        if (!worldIsValid(world)) {
            errors.add("World is not valid, please check again.");

            return Views.forward("/WEB-INF/jsp/hello-world-form.jsp")
                    .bind(bindings.typedValues()); // Re-bind values to view.
        }

        // Save the bean.
        WorldBean savedWorld = service.add(world);

        return Views.forward("/WEB-INF/jsp/hello-world-success.jsp")
                .bind("savedWorld", savedWorld);
    }
    
    /**
     * Simple check method which could do some evaluation.
     * @param world
     * @return isValid
     */
    protected boolean worldIsValid(WorldBean world) {
        // Some evaluation ...
        return world == null ? false : true;
    }
}
```

### The Form Bean

```java
public class WorldBean {

    protected Long id;

    @Required
    @On("/save-world-form")
    protected String name;

    @Check(required = true, minLength = 30, on = "/save-world-form")
    protected String description;

    @NotNull
    protected boolean isHumanLifeSupported;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHumanLifeSupported() {
        return isHumanLifeSupported;
    }

    public void setHumanLifeSupported(boolean humanLifeSupported) {
        isHumanLifeSupported = humanLifeSupported;
    }
}
```

### The JSP Page

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://geetools.org/jsp/geemvc/form" prefix="f"%>
<%@ taglib uri="http://geetools.org/jsp/geemvc/html" prefix="h"%>

<html>
	<head>
		<title>Hello World Example</title>

        <!-- CSS and JS files ... -->

	</head>
	<body>
    
		<h1>Hello World Form</h1>

		<!--
			Geemvc form tag. Optionally we specify which CSS classes are to be used when generating the HTML 
			for the form fields. This reduces a lot of boiler-plate-code and saves us from having to specify 
			them for each field, which is especially useful for larger forms. In this particular case we are 
			automatically creating bootstrap markup.
			
			We have left this example especially simple for you to quickly understand. Of course you can easily 
			create much more complex forms.
		-->
		<f:form action="/save-world-form" method="post" class="form-horizontal"
				fieldGroupClass="form-group row"
				fieldLabelClass="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label"
				fieldWrapperClass="col-xs-12 col-sm-6 col-md-6 col-lg-6"
				fieldClass="form-control"
				fieldHintClass="help-block"
				fieldErrorClass="help-block">

			<!-- Global errors -->
			<f:haserrors>
				<f:errors />
			</f:haserrors>

			<!-- Input field -->
			<f:text name="world.name" />

			<!-- Textarea field -->
			<f:textarea name="world.description" />

			<!-- Radio buttons -->
			<f:radio name="world.isHumanLifeSupported" value="true" />
			<f:radio name="world.isHumanLifeSupported" value="false" />

			<f:button name="world.save" type="submit">
				<h:message key="form.submit" />
			</f:button>

		</f:form>

<!--
        ### The generated HTML of a field will typically look like this (depending on the CSS classes that you define): ###
    
        <fieldset id="fs-el-world-name" class="form-field form-group row">
            <label for="el-world-name" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">Name</label>
            <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <input id="el-world-name" name="world.name" value="" class="form-el form-control" type="text">
                
                ### This line is added when a validation errors exists for the field. ###
                <small class="error help-block">Please enter a value for the field 'email'.</small>            
            </div>
        </fieldset>
-->
	</body>
</html>
```
