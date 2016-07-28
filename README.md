# geeMVC Java 8+ MVC-framework

[![Build Status](https://travis-ci.org/commerceboard/geemvc.svg?branch=master)](https://travis-ci.org/commerceboard/geemvc)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.geetools.geemvc/geemvc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.geetools.geemvc/geemvc/)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

geeMVC is a fast lightweight **MVC-framework** written for **Java 8+**. Its main focus is to be fast, simple to use and easy to extend. All classes within geeMVC have been injected by Google-Guice, making it the most adaptable and flexible Java MVC-framework. In order to simplify extending geeMVC we have ensured that no private or static fields and methods exist, therefore making it the No. 1 choice, not only for webapp developers, but also for framework developers that need a good starting point. 

geeMVC allows you to create webapps quickly, ranging from simple CRUD websites to large dynamic multi tenancy SaaS applications.

Check out our [motivation](https://github.com/commerceboard/geemvc/wiki/Motivation-Behind-geeMVC) behind creating geeMVC or jump straight to the ["Getting Started"](https://github.com/commerceboard/geemvc/wiki/Getting-Started) section.

## Why geeMVC?

* Because it is very simple to use and learn.
* Because there is no complex XML-configuration required. 
* Because geeMVC is very easy to extend with minimal fuss.
* Because it is fun to use and lets you get your work done fast.
* Because it comes with a very flexible routing and validation mechanism that is still easy to use.
* Because geeMVC allows you to use scripting languages like javascript, groovy or MVEL for even more flexibility.
* Because over 200 test-cases prove that it works reliably.

## Prerequisites

* **Java 8+.**
* **Java 8+ compatible servlet container.**

## Quick Start Guide
1) Add the following lines to the pom.xml of your webapp.

```xml
<dependency>
    <groupId>com.geetools.geemvc</groupId>
    <artifactId>geemvc</artifactId>
    <version>0.9.1-beta4</version>
</dependency>
```

2) Add the following servlet configuration to your web.xml.
```xml
	<servlet>
		<servlet-name>geeMVC-Servlet</servlet-name>
		<servlet-class>com.geemvc.DispatcherServlet</servlet-class>
		<!-- Base location of the jsp pages or templates. -->
		<init-param>
			<param-name>view-prefix</param-name>
			<param-value>/jsp/pages</param-value>
		</init-param>
		<!-- File suffix of the jsp pages or templates. -->
		<init-param>
			<param-name>view-suffix</param-name>
			<param-value>.jsp</param-value>
		</init-param>
		<!-- Comma-separated list of supported locales. -->
		<init-param>
			<param-name>supported-locales</param-name>
			<param-value>en, de</param-value>
		</init-param>
		<!-- Default character encoding. -->
		<init-param>
			<param-name>default-character-encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<!-- Default content-type of response body. -->
		<init-param>
			<param-name>default-content-type</param-name>
			<param-value>text/html</param-value>
		</init-param>
		<!-- Optionally use a custom Guice Injector for finding controllers and other objects etc. -->
		<init-param>
			<param-name>injector-provider</param-name>
			<param-value>com.custom.project.inject.MyInjectorProvider</param-value>
		</init-param>
		<!-- Optionally exclude paths from geeMVC servlet -->
		<init-param>
			<param-name>exclude-path-mapping</param-name>
			<param-value>/path-to-exclude/**</param-value>
		</init-param>
		<load-on-startup>10</load-on-startup>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>geeMVC-Servlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
```
3) Create your first controller by following the next step.

4) Enter the new URL into your browser. This will most likely be something like http://localhost:8080/hello/world if you have copied the example controller underneath.

> This README is kept especially simple so that you can get a quick overview. It is therefore highly recommended that you referr to the [example webapp](https://github.com/commerceboard/geemvc/tree/master/examples/webapp-jpa-jsp) and our extensive WIKI (currently in progress!) for more information.

## Example Controller
Simply annotate your controller and handler method and geeMVC will automatically find it.

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
geeMVC will automatically parse specified parameters out of the request URI and make them available to the handler method.

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

You can easily create your own annotations that are automatically evaluated by geeMVC. Please refer to the wiki page for more information.

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
Here we are getting some fictitious value from an injected service and passing it to the view - our JSP page. Notice that previously we simply returned a string informing geeMVC where we want to forward the request to. In the following example we have exchanged the string for a geeMVC "View" object. This allows you to bind values to the view that you will be able to access in your JSP page or templating engine.

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
The simplest way of validating your parameters is to use the javax.validation annotations or two additional ones provided by geeMVC (@Required and @Check). Notice in the example below the @Required annotation and the onError attribute that we have passed to the @Request annotation. The latter tells geeMVC where to go incase of a validation error. @Required obviously means that the id parameter must not be empty. Note that we are talking of "none-empty". This is useful for strings which are usually never null as empty form-fields get sent to the controller as an empty string. If you simply need to to check for "not null", you can use the javax.validation annotation @NotNull.

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
     * Because we have specified the "onError" attribute in the @Request annotation, geeMVC will not enter this
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
geeMVC automatically passes two objects into your handler method in order to let you check if any validation errors exist. For this, simply add the "Bindings" and/or "Errors" object(s) to your method signature.

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
            
            // Now we tell geeMVC where to go in case of a validation error.
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

            // Now we tell geeMVC where to go in case of a validation error.
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
<%@ taglib uri="http://geetools.com/jsp/geemvc/form" prefix="f"%>
<%@ taglib uri="http://geetools.com/jsp/geemvc/html" prefix="h"%>

<html>
	<head>
		<title>Hello World Example</title>

        <!-- CSS and JS files ... -->

	</head>
	<body>
    
		<h1>Hello World Form</h1>

		<!--
			geeMVC form tag. Optionally we specify which CSS classes are to be used when generating the HTML 
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

## Injecting geeMVC Objects into your Controller
Currently there are three useful helper objects that you can inject into your controller or any other injected class. These are:

| Type | Description |
| --- | --- |
| @Inject Cache cache | Enables you to cache and retrieve data. The current caching implementation uses the Google cache from Google Guava. |
| @Inject Injector injector | Injects the Google Guice injector for manually injecting objects. |
| @Logger Log log | Injects the standard logging implementation. geeMVC uses SLF4J and Logback-classic behind the scenes. |

Check out this example of how these objects can be automatically injected:

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;
    
    // ...
    
    @Request("/world/{id}")
    public String helloWorld(@PathParam Long id) {
    	// Use the logger.
        log.debug("Well done! You can now use the id path parameter in your code. The id is: {}", id);
        
        // Add or retrieve some value from the cache.
        cache.put("my-key", "my-value");

        // Inject your object.
        WorldBean world = injector.getInstance(WorldBean.class);

        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}
```

## Injecting geeMVC Objects into your Handler Method
In earlier examples we saw two object types that you can inject into your handler method. The following table shows all of the objects that can be included in the same fashion:

| Type | Description |
| --- | --- |
| javax.servlet.ServletRequest | The current servlet request object. |
| javax.servlet.ServletResponse | The current servlet response object. |
| javax.servlet.ServletContext | The current servlet context object. |
| java.util.Map&lt;String, String[]&gt; | The original request parameter map. |
| javax.servlet.http.HttpSession | The current http session object. |
| javax.servlet.http.Cookie[] | The current Cookie array retrieved from the servlet request object. |
| java.util.Locale | The current locale derermined by locale string passed in from browser and the configured available locales. |
| com.cb.geemvc.validation.Errors | Object where all validation errors of the current request are stored. |
| com.cb.geemvc.i18n.notice.Notices | Object for passing informational messages to the view. |
| com.cb.geemvc.Bindings | Object that holds all of the incoming bindings of the current request.|

### Setting a Cookie Value using the Injected Response Object
Injecting the above objects provide you with a number of possibilities. For example, by injecting the servlet response object you can send cookies to the client.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    /**
     * Automatically inject the response object into this handler method.
     */
    @Request("/cookie-test")
    public String cookieTest(HttpServletResponse response) {
        // Add the cookie to the response.
        response.addCookie(new Cookie("my-key", "my-value"));

        // Redirect to some URI.
        return "redirect: /cookie-success";
    }
}
```

## Using Interceptors
geeMVC offers two types of interceptors:

| Interceptor Type | Description |
| --- | --- |
| Around Interceptor | The around interceptor wraps your handler method allowing you to make changes before and after it is called. |
| Lifecycle Interceptor | The lifecycle interceptor lets you intercept various stages of the request lifecycle in geeMVC. |

### The Around Interceptor
As an example we will create an around interceptor that simply measures the time taken to process the handler method. Note that you must not forget to call "invocationCtx.proceed()" or your handler method will not get called. Also do not forget to return the result that your request handler has provided.

```java
@Intercept
public class TimerInterceptor implements AroundHandler {

    @Logger
    protected Log log;

    @Override
    public Object invokeAround(InvocationContext invocationCtx) {
        long start = System.currentTimeMillis();

        Object o = invocationCtx.proceed();

        log.trace("The request handler '{}' took {}ms.", () -> invocationCtx.requestHandler().toGenericString(), () -> System.currentTimeMillis() - start);

        return o;
    }
}
```

### The Lifecycle Interceptor

There are two ways of intercepting a lifecycle stage:

* In a separate controller class.
* As a method in your controller.

As we are providing you with a short README here, we will only show two quick examples at this point. Feel free to check out the wiki pages for more information.

First we will start by introducing you to the available lifecycle stages that you can intercept:

| Lifecycle Interceptor Type | Description |
| --- | --- |
| PreBinding | The pre-binding lifecycle interceptor gets called before request parameters are converted to typed values. |
| PostBinding | After type conversion has taken place. |
| PreValidation | The pre-validation lifecycle interceptor is called before validation takes place. |
| PostValidation | After validation takes place, allowing you to intercept the Errors object. |
| PreHandle | The pre-handle interceptor is called immediately before the actual request handler method is called. |
| PostHandle | After the request handler method has been processed. |
| PreView | The pre-view lifecycle interceptor is called immediately before the request is forwarded to the view. |
| PostView | After the view has been processed. |

#### Intercepting a Lifecycle Stage in a Class
In the following example we use the interceptor to bind countries and languages to the view before geeMVC forwards to it. There are 3 main annotations that tell geeMVC if the interceptor is to be activated or not:

| Annotation Attribute | Description | Options |
| --- | --- | --- |
| on | Filters which request handler path mapping is to be included. | None |
| onView | Only triggers the interceptor when the view is in a particular state. | EXISTS, NOT_EXISTS, ALWAYS |
| when | Only triggers the interceptor when the Errors object is in a particular state. | NO_ERRORS, HAS_ERRORS, ALWAYS |

Alternatively to the "on" attribute you can also filter the lifecycle interceptor by controller-class, handler-method or unique handler-name.

```java
@PreView(on = {"/form-one", "/form-two", "/form-three"}, onView = OnView.EXISTS, when = When.NO_ERRORS)
public class preViewInterceptor {
    @Inject
    protected Service someService;

    /**
     * Add countries and languages before forwarding to view.
     */
    public void intercept(View view) {
        view.bind("countries", service.getCountries())
                .bind("languages", service.getLanguages());
    }
}
```

#### Intercepting a Lifecycle Stage in a Controller Method

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Logger
    protected Log log;

    // ....

    @PostValidation(on = {"/save-world-form"}, when = When.HAS_ERRORS)
    public void afterValidation(LifecycleContext lifecycleCtx, Errors errors) {
        log.trace("Validation has found {} errors.", () -> errors.allErrors().size());

        // Do something ...
    }
    
    // ....
    
}
```
