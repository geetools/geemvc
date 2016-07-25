<%@page import="com.cb.examples.jpajsp.geeticket.controller.FormPlaygroundController2"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://geetools.com/jsp/geemvc/form" prefix="f"%>
<%@ taglib uri="http://geetools.com/jsp/geemvc/html" prefix="h"%>

<h1>Form Playground</h1>

<c:set value="1234567890" var="id"></c:set>

<f:form name="unique-name-1" p_id="${id}" method="post">

	&lt;f:form name="unique-name-1" p_id="${id}"&gt;<br />
	<br />

	&lt;f:form action="/forms/save/${id}" method="post" name="manually-set-html-form-name" id="manually-set-html-form-id"&gt;<br />
	<br />

	&lt;f:form handler="saveForm"&gt;<br />
	<br />

	&lt;f:form controllerClass="&lt;%=FormPlaygroundController2.class %&gt;" handler="saveForm"&gt;<br />
	<br />

	&lt;f:form controller="com.cb.examples.jpajsp.geeticket.controller.FormPlaygroundController2" handler="saveForm"&gt;<br />
	<br />

	<h2>Messages</h2>
	<p>
		Message: &lt;h:message key="selectedTypes"&gt;My default text&lt;/h:message&gt;<br />
	</p>
	Message: <h:message key="selectedTypes">My default text</h:message>
	<br />
	<br />
	<p>
		&lt;h:message key="ticket.title" var="titleText"&gt;My default title&lt;/h:message&gt;<br /> Message: $&lcub;titleText&rcub;
	</p>
	<h:message key="ticket.title" var="titleText">My default title</h:message>
	Message: ${titleText}<br />
	<br />

	<p>
		Message: &lt;h:message key="ticket.description"&gt;My default description&lt;/h:message&gt;<br />
	</p>
	Message: <h:message key="ticket.description">My default description</h:message>
	<br />
	<br />

	<p>
		&lt;c:set value="one" var="param1" /&gt;<br />
		&lt;c:set value="two" var="param2" /&gt;<br />
		Message: &lt;h:message key="text.with.params" p0="$&lcub;param1&rcub;" p1="$&lcub;param2&rcub;"&gt;My default description with param ''{0}'' and ''{1}''&lt;/h:message&gt;<br />
	</p>

	<c:set value="one" var="param1" />
	<c:set value="two" var="param2" />

	Message: <h:message key="text.with.params" p0="${param1}" p1="${param2}">My default description with param ''{0}'' and ''{1}''</h:message>
	<br />

	<h2>Urls</h2>
	<p>&lt;h:url value="/forms/playground"&gt;My link using the value attribute&lt;/h:url&gt;</p>
	<h:url value="/forms/playground">My link using the value attribute</h:url>
	<br />
	<br />

	<p>&lt;h:url name="unique-name-3"&gt;My link using a unique name&lt;/h:url&gt;</p>
	<h:url name="unique-name-3">My link using a unique name</h:url>
	<br />
	<br />

	<p>&lt;h:url handler="saveForm3"&gt;My link using a handler name from the current controller&lt;/h:url&gt;</p>
	<h:url handler="saveForm3">My link using a handler name from the current controller</h:url>
	<br />
	<br />

	<p>
		&lt;h:url controllerClass="&lt;%=FormPlaygroundController2.class %&gt;" handler="playground"&gt;My link using a handler from another controller&lt;/h:url&gt;<br /> &lt;h:url controller="com.cb.examples.jpajsp.geeticket.controller.FormPlaygroundController2" handler="playground"&gt;My link using a handler from another controller&lt;/h:url&gt;
	</p>
	<h:url controllerClass="<%=FormPlaygroundController2.class%>" handler="playground">My link using a handler from another controller</h:url>
	<br />
	<br />

	<h2>Text Input</h2>
	<p>
		&lt;f:label name="ticket.title"&gt;Title:&lt;/f:label&gt;<br /> &lt;f:text name="ticket.title" /&gt;
	</p>
	<f:label name="ticket.title">Title:</f:label>
	<f:text name="ticket.title" />
	<br />

	<h2>Textarea</h2>
	<p>
		&lt;f:label name="ticket.description"&gt;Description:&lt;/f:label&gt;<br /> &lt;f:textarea name="ticket.description" /&gt;
	</p>
	<f:label name="ticket.descriptions">Description:</f:label>
	<br />
	<f:textarea name="ticket.description" />
	<br />

	<h2>Checkbox</h2>
	<p>
		&lt;f:checkbox name="selectedTypes[]" value="BUG" /&gt; &lt;f:label name="selectedTypes[]" value="BUG"&gt;Bug&lt;/f:label&gt;<br /> &lt;f:checkbox name="selectedTypes[]" value="FEATURE" /&gt; &lt;f:label name="selectedTypes[]" value="FEATURE"&gt;Feature&lt;/f:label&gt;<br /> &lt;f:checkbox name="selectedTypes[]" value="TASK" /&gt; &lt;f:label name="selectedTypes[]" value="TASK"&gt;Task&lt;/f:label&gt;
	<p>
		<f:checkbox name="selectedTypes[]" value="BUG" />
		<f:label name="selectedTypes[]" value="BUG">Bug</f:label>
		<br />
		<f:checkbox name="selectedTypes[]" value="FEATURE" />
		<f:label name="selectedTypes[]" value="FEATURE">Feature</f:label>
		<br />
		<f:checkbox name="selectedTypes[]" value="TASK" />
		<f:label name="selectedTypes[]" value="TASK">Task</f:label>
		<br /> <br />
	<h2>Radio</h2>
	<p>
		&lt;f:radio name="ticket.type" value="BUG" /&gt; &lt;f:label name="ticket.type" value="BUG"&gt;Bug&lt;/f:label&gt;<br /> &lt;f:radio name="ticket.type" value="FEATURE" /&gt; &lt;f:label name="ticket.type" value="FEATURE"&gt;Feature&lt;/f:label&gt;<br /> &lt;f:radio name="ticket.type" value="TASK" /&gt; &lt;f:label name="ticket.type" value="TASK"&gt;Task&lt;/f:label&gt;<br />
	<p>
		<f:radio name="ticket.type" value="BUG" />
		<f:label name="ticket.type" value="BUG">Bug</f:label>
		<br />
		<f:radio name="ticket.type" value="FEATURE" />
		<f:label name="ticket.type" value="FEATURE">Feature</f:label>
		<br />
		<f:radio name="ticket.type" value="TASK" />
		<f:label name="ticket.type" value="TASK">Task</f:label>
		<br /> <br />
	<h2>Select with Manually Filled Options</h2>
	<p>
		&lt;f:label name="ticket.type"&gt;Type:&lt;/f:label&gt;<br /> &lt;f:select name="ticket.type"><br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="BUG"&gt;Bug&lt;/f:option&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="FEATURE"&gt;Feature&lt;/f:option&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="TASK"&gt;Task&lt;/f:option&gt;<br /> &lt;/f:select>
	<p>

		<f:label name="ticket.type">Type:</f:label>
		<br />
		<f:select name="ticket.type">
			<f:option value="BUG">Bug</f:option>
			<f:option value="FEATURE">Feature</f:option>
			<f:option value="TASK">Task</f:option>
		</f:select>
		<br />
	<h2>Multi-Select with Manually Filled Options</h2>
	<p>
		&lt;f:select name="selectedTypes" multiple="multiple" size="5"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="BUG"&gt;Bug&lt;/f:option&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="FEATURE"&gt;Feature&lt;/f:option&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:option value="TASK"&gt;Task&lt;/f:option&gt;<br /> &lt;/f:select>
	<p>

		<f:label name="selectedTypes"></f:label>
		<br />
		<f:select name="selectedTypes" multiple="multiple" size="5">
			<f:option value="BUG">Bug</f:option>
			<f:option value="FEATURE">Feature</f:option>
			<f:option value="TASK">Task</f:option>
		</f:select>
		<br />
	<h2>Select with Values from User Collection</h2>
	<p>
		&lt;f:select name="ticket.assignee.id"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="userList" value="id" label="name" /&gt;<br /> &lt;/f:select&gt;
	</p>

	<f:label name="ticket.assignee.id"></f:label>
	<br />
	<f:select name="ticket.assignee.id">
		<f:options values="userList" value="id" label="name" />
	</f:select>
	<br />

	<h2>Select with Values from User Enumeration</h2>
	<p>
		&lt;f:select name="ticket.assignee.id"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="userEnumeration" value="id" label="name" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="ticket.assignee.id">
		<f:options values="userEnumeration" value="id" label="name" />
	</f:select>
	<br />

	<h2>Select with Values from String Collection</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringValues" />
	</f:select>
	<br />

	<h2>Select with Values from Integer Collection</h2>
	<p>
		&lt;f:select name="intVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="intVal">
		<f:options values="integerValues" />
	</f:select>
	<br />

	<h2>Select with Values from String Array</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringArrayValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringArrayValues" />
	</f:select>
	<br />

	<h2>Select with Values from Integer Array</h2>
	<p>
		&lt;f:select name="intVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerArrayValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="intVal">
		<f:options values="integerArrayValues" />
	</f:select>
	<br />


	<h2>Select with Values from String Enumeration</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringEnumerationValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringEnumerationValues" />
	</f:select>
	<br />

	<h2>Select with Values from Integer Enumeration</h2>
	<p>
		&lt;f:select name="intVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerEnumerationValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="intVal">
		<f:options values="integerEnumerationValues" />
	</f:select>
	<br />


	<h2>Select with Values from String Map #1</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringMapValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringMapValues" />
	</f:select>
	<br />

	<h2>Select with Values from String Map #2</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringMapValues" label="key::this" value="value::this" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringMapValues" label="key::this" value="value::this" />
	</f:select>
	<br />

	<h2>Select with Values from String Map #3</h2>
	<p>
		&lt;f:select name="strVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="stringMapValues" label="value::this" value="key::this" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="strVal">
		<f:options values="stringMapValues" label="value::this" value="key::this" />
	</f:select>
	<br />

	<h2>Select with Values from Integer Map</h2>
	<p>
		&lt;f:select name="intVal"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerMapValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="intVal">
		<f:options values="integerMapValues" />
	</f:select>
	<br />

	<h2>Select with Values from Long/User Map #1</h2>
	<p>
		&lt;f:select name="ticket.assignee.id"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="userMapValues" label="name" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="ticket.assignee.id">
		<f:options values="userMapValues" label="name" />
	</f:select>
	<br />


	<h2>Select with Values from Long/User Map #2</h2>
	<p>
		&lt;f:select name="ticket.assignee.id"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="userMapValues" label="value::name" value="value::id" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="ticket.assignee.id">
		<f:options values="userMapValues" label="value::name" value="value::id" />
	</f:select>
	<br />

	<h2>Select with Values from Long/User Map #3</h2>
	<p>
		&lt;f:select name="ticket.assignee.id"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="userMapValues" label="value::this" value="key::this" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="ticket.assignee.id">
		<f:options values="userMapValues" label="value::this" value="key::this" />
	</f:select>
	<br />


	<h2>Multi-Select with Values from Ticket Tags</h2>
	<p>
		&lt;f:select name="ticket.tags" multiple="multiple" size="5"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="allTags" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="ticket.tags" multiple="multiple" size="5">
		<f:options values="allTags" />
	</f:select>
	<br />

	<h2>Multi-Select with Values from TestBean intValues</h2>
	<p>
		&lt;f:select name="testBean.intValues" multiple="multiple" size="5"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="testBean.intValues" multiple="multiple" size="5">
		<f:options values="integerValues" />
	</f:select>
	<br />

	<h2>Multi-Select with Values from TestBean intValues</h2>
	<p>
		&lt;f:select name="testBean.intValues" multiple="multiple" size="5"&gt;<br /> &nbsp;&nbsp;&nbsp;&lt;f:options values="integerMapValues" /&gt;<br /> &lt;/f:select&gt;
	</p>
	<f:select name="testBean.intValues" multiple="multiple" size="5">
		<f:options values="integerMapValues" />
	</f:select>
	<br />



</f:form>

<br />
<br />
<br />
<br />
<br />