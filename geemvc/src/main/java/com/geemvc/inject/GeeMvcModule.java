/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geemvc.inject;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.RuntimeDelegate;

import com.geemvc.Bindings;
import com.geemvc.DefaultBindings;
import com.geemvc.DefaultInternalRequestContext;
import com.geemvc.DefaultRequestContext;
import com.geemvc.DefaultRequestRunner;
import com.geemvc.InternalRequestContext;
import com.geemvc.RequestContext;
import com.geemvc.RequestRunner;
import com.geemvc.bind.DefaultMethodParam;
import com.geemvc.bind.DefaultMethodParams;
import com.geemvc.bind.DefaultPropertyNode;
import com.geemvc.bind.MethodParam;
import com.geemvc.bind.MethodParams;
import com.geemvc.bind.PropertyNode;
import com.geemvc.bind.param.DefaultParamAdapterFactory;
import com.geemvc.bind.param.DefaultParamAdapterKey;
import com.geemvc.bind.param.DefaultParamAdapters;
import com.geemvc.bind.param.DefaultParamContext;
import com.geemvc.bind.param.ParamAdapterFactory;
import com.geemvc.bind.param.ParamAdapterKey;
import com.geemvc.bind.param.ParamAdapters;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.cache.Cache;
import com.geemvc.cache.CacheEntry;
import com.geemvc.cache.DefaultCache;
import com.geemvc.cache.DefaultCacheEntry;
import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.converter.BeanConverter;
import com.geemvc.converter.ConverterAdapterFactory;
import com.geemvc.converter.ConverterAdapterKey;
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.DefaultBeanConverter;
import com.geemvc.converter.DefaultConverterAdapterFactory;
import com.geemvc.converter.DefaultConverterAdapterKey;
import com.geemvc.converter.DefaultConverterContext;
import com.geemvc.converter.DefaultSimpleConverter;
import com.geemvc.converter.SimpleConverter;
import com.geemvc.data.DataAdapterFactory;
import com.geemvc.data.DefaultDataAdapterFactory;
import com.geemvc.handler.CompositeControllerResolver;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.handler.DefaultCompositeControllerResolver;
import com.geemvc.handler.DefaultCompositeHandlerResolver;
import com.geemvc.handler.DefaultHandlerResolutionPlan;
import com.geemvc.handler.DefaultRequestHandler;
import com.geemvc.handler.DefaultRequestHandlerKey;
import com.geemvc.handler.DefaultRequestHandlers;
import com.geemvc.handler.DefaultRequestMappingKey;
import com.geemvc.handler.DefaultSimpleControllerResolver;
import com.geemvc.handler.DefaultSimpleHandlerResolver;
import com.geemvc.handler.HandlerResolutionPlan;
import com.geemvc.handler.RequestHandler;
import com.geemvc.handler.RequestHandlerKey;
import com.geemvc.handler.RequestHandlers;
import com.geemvc.handler.RequestMappingKey;
import com.geemvc.handler.SimpleControllerResolver;
import com.geemvc.handler.SimpleHandlerResolver;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.Controllers;
import com.geemvc.helper.DefaultAnnotations;
import com.geemvc.helper.DefaultControllers;
import com.geemvc.helper.DefaultMimeTypes;
import com.geemvc.helper.DefaultPaths;
import com.geemvc.helper.DefaultRequests;
import com.geemvc.helper.DefaultStrings;
import com.geemvc.helper.DefaultUriBuilder;
import com.geemvc.helper.MimeTypes;
import com.geemvc.helper.Paths;
import com.geemvc.helper.Requests;
import com.geemvc.helper.Strings;
import com.geemvc.helper.UriBuilder;
import com.geemvc.i18n.locale.DefaultLocaleResolver;
import com.geemvc.i18n.locale.LocaleResolver;
import com.geemvc.i18n.message.CompositeMessageResolver;
import com.geemvc.i18n.message.DefaultCompositeMessageResolver;
import com.geemvc.i18n.message.DefaultMessages;
import com.geemvc.i18n.message.DefaultSimpleMessageResolver;
import com.geemvc.i18n.message.Messages;
import com.geemvc.i18n.message.SimpleMessageResolver;
import com.geemvc.i18n.notice.DefaultNotice;
import com.geemvc.i18n.notice.DefaultNotices;
import com.geemvc.i18n.notice.Notice;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.intercept.DefaultInterceptorResolver;
import com.geemvc.intercept.DefaultInterceptors;
import com.geemvc.intercept.DefaultInvocationContext;
import com.geemvc.intercept.DefaultLifecycleContext;
import com.geemvc.intercept.DefaultLifecycleInterceptor;
import com.geemvc.intercept.InterceptorResolver;
import com.geemvc.intercept.Interceptors;
import com.geemvc.intercept.InvocationContext;
import com.geemvc.intercept.LifecycleContext;
import com.geemvc.intercept.LifecycleInterceptor;
import com.geemvc.logging.DefaultLog;
import com.geemvc.logging.Log;
import com.geemvc.logging.LoggerTypeListener;
import com.geemvc.matcher.CookieMatcher;
import com.geemvc.matcher.DefaultCookieMatcher;
import com.geemvc.matcher.DefaultHandlesMatcher;
import com.geemvc.matcher.DefaultHeaderMatcher;
import com.geemvc.matcher.DefaultMatcherContext;
import com.geemvc.matcher.DefaultParamMatcher;
import com.geemvc.matcher.DefaultPathMatcher;
import com.geemvc.matcher.DefaultPathMatcherKey;
import com.geemvc.matcher.HandlesMatcher;
import com.geemvc.matcher.HeaderMatcher;
import com.geemvc.matcher.MatcherContext;
import com.geemvc.matcher.ParamMatcher;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.reflect.DefaultReflectionProvider;
import com.geemvc.reflect.DefaultReflectionsWrapper;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.reflect.ReflectionsWrapper;
import com.geemvc.rest.jaxrs.DefaultProviderFilter;
import com.geemvc.rest.jaxrs.DefaultProviderKey;
import com.geemvc.rest.jaxrs.DefaultProviders;
import com.geemvc.rest.jaxrs.JaxRsApplication;
import com.geemvc.rest.jaxrs.ProviderFilter;
import com.geemvc.rest.jaxrs.ProviderKey;
import com.geemvc.rest.jaxrs.context.DefaultHttpHeaders;
import com.geemvc.rest.jaxrs.context.DefaultResponse;
import com.geemvc.rest.jaxrs.context.DefaultUriInfo;
import com.geemvc.rest.jaxrs.context.GeeMvcResponse;
import com.geemvc.rest.jaxrs.delegate.DefaultMediaTypeHeaderDelegate;
import com.geemvc.rest.jaxrs.delegate.DefaultResponseBuilder;
import com.geemvc.rest.jaxrs.delegate.DefaultRuntimeDelegate;
import com.geemvc.rest.jaxrs.delegate.MediaTypeHeaderDelegate;
import com.geemvc.rest.jaxrs.util.DefaultMultivaluedMap;
import com.geemvc.rest.jaxrs.util.DefaultObjectFactory;
import com.geemvc.rest.jaxrs.util.ObjectFactory;
import com.geemvc.script.DefaultEvaluatorContext;
import com.geemvc.script.DefaultEvaluatorFactory;
import com.geemvc.script.DefaultRegex;
import com.geemvc.script.DefaultSimpleEvaluator;
import com.geemvc.script.EvaluatorContext;
import com.geemvc.script.EvaluatorFactory;
import com.geemvc.script.GroovyEvaluator;
import com.geemvc.script.Regex;
import com.geemvc.script.ScriptEvaluator;
import com.geemvc.script.SimpleEvaluator;
import com.geemvc.validation.DefaultError;
import com.geemvc.validation.DefaultErrors;
import com.geemvc.validation.DefaultValidation;
import com.geemvc.validation.DefaultValidationAdapterFactory;
import com.geemvc.validation.DefaultValidationAdapterKey;
import com.geemvc.validation.DefaultValidationContext;
import com.geemvc.validation.DefaultValidations;
import com.geemvc.validation.DefaultValidator;
import com.geemvc.validation.DefaultViewOnlyRequestHandler;
import com.geemvc.validation.Error;
import com.geemvc.validation.Errors;
import com.geemvc.validation.Validation;
import com.geemvc.validation.ValidationAdapterFactory;
import com.geemvc.validation.ValidationAdapterKey;
import com.geemvc.validation.ValidationContext;
import com.geemvc.validation.Validations;
import com.geemvc.validation.Validator;
import com.geemvc.validation.ViewOnlyRequestHandler;
import com.geemvc.view.DefaultStreamViewHandler;
import com.geemvc.view.DefaultViewAdapterFactory;
import com.geemvc.view.DefaultViewHandler;
import com.geemvc.view.StreamViewHandler;
import com.geemvc.view.ViewAdapterFactory;
import com.geemvc.view.ViewHandler;
import com.geemvc.view.bean.DefaultResult;
import com.geemvc.view.bean.Result;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class GeeMvcModule extends AbstractModule {

    @Override
    protected void configure() {
        configureLog();
        configureLoggerTypeListener();
        configureCache();
        configureCacheEntry();
        configureRequestRunner();
        configureRequestContext();
        configurePathOnlyRequestContext();
        configureCompositeControllerResolver();
        configureSimpleControllerResolver();
        configureReflectionProvider();
        configureReflectionsWrapper();
        configureAnnotations();
        configureControllers();
        configurePathMatcher();
        configureParamMatcher();
        configureHeaderMatcher();
        configureCookieMatcher();
        configureHandlesMatcher();
        configurePathMatcherKey();
        configureMatcherContext();
        configurePathRegex();
        configureRequestHandler();
        configureRequestHandlers();
        configureRequestHandlerKey();
        configureRequestMappingKey();
        configureCompositeHandlerResolver();
        configureSimpleHandlerResolver();
        configureHandlerResolverStats();
        configureEvaluatorFactory();
        configureEvaluatorContext();
        configureSimpleEvaluator();
        configureScriptEvaluator();
        configureStringHelper();
        configureRequestHelper();
        configureMimeTypeHelper();
        configurePathHelper();
        configureConverterAdapterFactory();
        configureConverterAdapterKey();
        configureConverterContext();
        configureSimpleConverter();
        configureBeanConverter();
        configureParamAdapterFactory();
        configureParamAdapterKey();
        configureParamAdapters();
        configureParamContext();
        configureMethodParams();
        configureMethodParam();
        configurePropertyNode();
        configureViewBean();
        configureViewAdapterFactory();
        configureViewHandler();
        configureDataAdapterFactory();
        configureLocaleResolver();
        configureCompositeMessageResolvers();
        configureSimpleMessageResolvers();
        configureInterceptorResolver();
        configureInterceptors();
        configureInvocationContext();
        configureLifecycleInterceptor();
        configureLifecycleContext();
        configureNotice();
        configureNotices();
        configureError();
        configureErrors();
        configureMessages();
        configureValidator();
        configureValidation();
        configureValidations();
        configureValidationContext();
        configureValidationAdapterKey();
        configureValidationAdapterFactory();
        configureBindings();
        configureViewOnlyRequestHandler();
        configureStreamViewHandler();
        configureUriBuilder();

        // Jax-RS
        configureJaxRSRuntimeDelegate();
        configureJaxRSApplication();
        configureJaxRSProviders();
        configureJaxRSProviderFilter();
        configureJaxRSProviderKey();
        configureJaxRSMultivaluedMap();
        configureJaxRSHttpHeaders();
        configureJaxRSUriInfo();
        configureJaxRSObjectFactory();
        configureJaxRSResponse();
        configureJaxRSResponseBuilder();
        configureJaxRSMediaTypeHeaderDelegate();
    }

    protected void configureJaxRSResponse() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(GeeMvcResponse.class).to(DefaultResponse.class);
    }

    protected void configureJaxRSResponseBuilder() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(ResponseBuilder.class).to(DefaultResponseBuilder.class);
    }

    protected void configureJaxRSMediaTypeHeaderDelegate() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(MediaTypeHeaderDelegate.class).to(DefaultMediaTypeHeaderDelegate.class);
    }

    protected void configureJaxRSRuntimeDelegate() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(RuntimeDelegate.class).to(DefaultRuntimeDelegate.class);
    }

    protected void configureJaxRSApplication() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(Application.class).to(JaxRsApplication.class);
    }

    protected void configureJaxRSProviders() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(Providers.class).to(DefaultProviders.class);
    }

    protected void configureJaxRSProviderFilter() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(ProviderFilter.class).to(DefaultProviderFilter.class);
    }

    protected void configureJaxRSProviderKey() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(ProviderKey.class).to(DefaultProviderKey.class);
    }

    protected void configureJaxRSMultivaluedMap() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(MultivaluedMap.class).to(DefaultMultivaluedMap.class);
    }

    protected void configureJaxRSHttpHeaders() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(HttpHeaders.class).to(DefaultHttpHeaders.class);
    }

    protected void configureJaxRSUriInfo() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(UriInfo.class).to(DefaultUriInfo.class);
    }

    protected void configureJaxRSObjectFactory() {
        if (configuration() == null || configuration().isJaxRsEnabled())
            bind(ObjectFactory.class).to(DefaultObjectFactory.class);
    }

    protected void configureLog() {
        bind(Log.class).to(DefaultLog.class);
    }

    protected void configureLoggerTypeListener() {
        bindListener(Matchers.any(), new LoggerTypeListener());
    }

    protected void configureViewOnlyRequestHandler() {
        bind(ViewOnlyRequestHandler.class).to(DefaultViewOnlyRequestHandler.class);
    }

    protected void configureBindings() {
        bind(Bindings.class).to(DefaultBindings.class);
    }

    protected void configureValidationAdapterFactory() {
        bind(ValidationAdapterFactory.class).to(DefaultValidationAdapterFactory.class);
    }

    protected void configureValidationAdapterKey() {
        bind(ValidationAdapterKey.class).to(DefaultValidationAdapterKey.class);
    }

    protected void configureValidationContext() {
        bind(ValidationContext.class).to(DefaultValidationContext.class);
    }

    protected void configureValidator() {
        bind(Validator.class).to(DefaultValidator.class);
    }

    protected void configureValidations() {
        bind(Validations.class).to(DefaultValidations.class);
    }

    protected void configureValidation() {
        bind(Validation.class).to(DefaultValidation.class);
    }

    protected void configureErrors() {
        bind(Errors.class).to(DefaultErrors.class);
    }

    protected void configureError() {
        bind(Error.class).to(DefaultError.class);
    }

    protected void configureNotices() {
        bind(Notices.class).to(DefaultNotices.class);
    }

    protected void configureNotice() {
        bind(Notice.class).to(DefaultNotice.class);
    }

    protected void configureMessages() {
        bind(Messages.class).to(DefaultMessages.class);
    }

    protected void configureStreamViewHandler() {
        bind(StreamViewHandler.class).to(DefaultStreamViewHandler.class);
    }

    protected void configureCompositeMessageResolvers() {
        bind(CompositeMessageResolver.class).to(DefaultCompositeMessageResolver.class);
    }

    protected void configureSimpleMessageResolvers() {
        bind(SimpleMessageResolver.class).to(DefaultSimpleMessageResolver.class);
    }

    protected void configureLocaleResolver() {
        bind(LocaleResolver.class).to(DefaultLocaleResolver.class);
    }

    protected void configureConverterAdapterFactory() {
        bind(ConverterAdapterFactory.class).to(DefaultConverterAdapterFactory.class);
    }

    protected void configureConverterAdapterKey() {
        bind(ConverterAdapterKey.class).to(DefaultConverterAdapterKey.class);
    }

    protected void configureConverterContext() {
        bind(ConverterContext.class).to(DefaultConverterContext.class);
    }

    protected void configureParamAdapterFactory() {
        bind(ParamAdapterFactory.class).to(DefaultParamAdapterFactory.class);
    }

    protected void configureParamAdapterKey() {
        bind(ParamAdapterKey.class).to(DefaultParamAdapterKey.class);
    }

    protected void configureParamAdapters() {
        bind(ParamAdapters.class).to(DefaultParamAdapters.class);
    }

    protected void configureParamContext() {
        bind(ParamContext.class).to(DefaultParamContext.class);
    }

    protected void configureSimpleConverter() {
        bind(SimpleConverter.class).to(DefaultSimpleConverter.class);
    }

    protected void configureBeanConverter() {
        bind(BeanConverter.class).to(DefaultBeanConverter.class);
    }

    protected void configureRequestHandler() {
        bind(RequestHandler.class).to(DefaultRequestHandler.class);
    }

    protected void configureRequestHandlers() {
        bind(RequestHandlers.class).to(DefaultRequestHandlers.class);
    }

    protected void configureMethodParams() {
        bind(MethodParams.class).to(DefaultMethodParams.class);
    }

    protected void configureMethodParam() {
        bind(MethodParam.class).to(DefaultMethodParam.class);
    }

    protected void configureCompositeHandlerResolver() {
        bind(CompositeHandlerResolver.class).to(DefaultCompositeHandlerResolver.class);
    }

    protected void configureSimpleHandlerResolver() {
        bind(SimpleHandlerResolver.class).to(DefaultSimpleHandlerResolver.class);
    }

    protected void configureHandlerResolverStats() {
        bind(HandlerResolutionPlan.class).to(DefaultHandlerResolutionPlan.class);
    }

    protected void configureRequestRunner() {
        bind(RequestRunner.class).to(DefaultRequestRunner.class);
    }

    protected void configureRequestContext() {
        bind(RequestContext.class).to(DefaultRequestContext.class);
    }

    protected void configurePathOnlyRequestContext() {
        bind(InternalRequestContext.class).to(DefaultInternalRequestContext.class);
    }

    protected void configureCache() {
        bind(Cache.class).to(DefaultCache.class);
    }

    protected void configureCacheEntry() {
        bind(CacheEntry.class).to(DefaultCacheEntry.class);
    }

    protected void configureCompositeControllerResolver() {
        bind(CompositeControllerResolver.class).to(DefaultCompositeControllerResolver.class);
    }

    protected void configureSimpleControllerResolver() {
        bind(SimpleControllerResolver.class).to(DefaultSimpleControllerResolver.class);
    }

    protected void configureReflectionProvider() {
        bind(ReflectionProvider.class).to(DefaultReflectionProvider.class);
    }

    protected void configureReflectionsWrapper() {
        bind(ReflectionsWrapper.class).to(DefaultReflectionsWrapper.class);
    }

    protected void configureAnnotations() {
        bind(Annotations.class).to(DefaultAnnotations.class);
    }

    protected void configureControllers() {
        bind(Controllers.class).to(DefaultControllers.class);
    }

    protected void configurePathMatcher() {
        bind(PathMatcher.class).to(DefaultPathMatcher.class);
    }

    protected void configureParamMatcher() {
        bind(ParamMatcher.class).to(DefaultParamMatcher.class);
    }

    protected void configureHeaderMatcher() {
        bind(HeaderMatcher.class).to(DefaultHeaderMatcher.class);
    }

    protected void configureCookieMatcher() {
        bind(CookieMatcher.class).to(DefaultCookieMatcher.class);
    }

    protected void configureHandlesMatcher() {
        bind(HandlesMatcher.class).to(DefaultHandlesMatcher.class);
    }

    protected void configurePathMatcherKey() {
        bind(PathMatcherKey.class).to(DefaultPathMatcherKey.class);
    }

    protected void configureMatcherContext() {
        bind(MatcherContext.class).to(DefaultMatcherContext.class);
    }

    protected void configureRequestHandlerKey() {
        bind(RequestHandlerKey.class).to(DefaultRequestHandlerKey.class);
    }

    protected void configureRequestMappingKey() {
        bind(RequestMappingKey.class).to(DefaultRequestMappingKey.class);
    }

    protected void configurePathRegex() {
        bind(Regex.class).to(DefaultRegex.class);
    }

    protected void configureEvaluatorFactory() {
        bind(EvaluatorFactory.class).to(DefaultEvaluatorFactory.class);
    }

    protected void configureEvaluatorContext() {
        bind(EvaluatorContext.class).to(DefaultEvaluatorContext.class);
    }

    protected void configureSimpleEvaluator() {
        bind(SimpleEvaluator.class).to(DefaultSimpleEvaluator.class);
    }

    protected void configureScriptEvaluator() {
        bind(ScriptEvaluator.class).to(GroovyEvaluator.class);
    }

    protected void configureStringHelper() {
        bind(Strings.class).to(DefaultStrings.class);
    }

    protected void configureRequestHelper() {
        bind(Requests.class).to(DefaultRequests.class);
    }

    protected void configureMimeTypeHelper() {
        bind(MimeTypes.class).to(DefaultMimeTypes.class);
    }

    protected void configurePathHelper() {
        bind(Paths.class).to(DefaultPaths.class);
    }

    protected void configurePropertyNode() {
        bind(PropertyNode.class).to(DefaultPropertyNode.class);
    }

    protected void configureViewBean() {
        bind(Result.class).to(DefaultResult.class);
    }

    protected void configureViewAdapterFactory() {
        bind(ViewAdapterFactory.class).to(DefaultViewAdapterFactory.class);
    }

    protected void configureViewHandler() {
        bind(ViewHandler.class).to(DefaultViewHandler.class);
    }

    protected void configureDataAdapterFactory() {
        bind(DataAdapterFactory.class).to(DefaultDataAdapterFactory.class);
    }

    protected void configureInterceptorResolver() {
        bind(InterceptorResolver.class).to(DefaultInterceptorResolver.class);
    }

    protected void configureInterceptors() {
        bind(Interceptors.class).to(DefaultInterceptors.class);
    }

    protected void configureInvocationContext() {
        bind(InvocationContext.class).to(DefaultInvocationContext.class);
    }

    protected void configureLifecycleInterceptor() {
        bind(LifecycleInterceptor.class).to(DefaultLifecycleInterceptor.class);
    }

    protected void configureLifecycleContext() {
        bind(LifecycleContext.class).to(DefaultLifecycleContext.class);
    }

    protected void configureUriBuilder() {
        bind(UriBuilder.class).to(DefaultUriBuilder.class);
    }

    protected Configuration configuration() {
        return (Configuration) Configurations.get();
    }
}
