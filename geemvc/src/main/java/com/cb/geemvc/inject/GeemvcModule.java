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

package com.cb.geemvc.inject;

import com.cb.geemvc.*;
import com.cb.geemvc.bind.*;
import com.cb.geemvc.bind.param.*;
import com.cb.geemvc.cache.Cache;
import com.cb.geemvc.cache.DefaultCache;
import com.cb.geemvc.config.Configuration;
import com.cb.geemvc.config.DefaultConfiguration;
import com.cb.geemvc.converter.*;
import com.cb.geemvc.data.DataAdapterFactory;
import com.cb.geemvc.data.DefaultDataAdapterFactory;
import com.cb.geemvc.handler.*;
import com.cb.geemvc.helper.*;
import com.cb.geemvc.i18n.locale.DefaultLocaleResolver;
import com.cb.geemvc.i18n.locale.LocaleResolver;
import com.cb.geemvc.i18n.message.CompositeMessageResolver;
import com.cb.geemvc.i18n.message.DefaultCompositeMessageResolver;
import com.cb.geemvc.i18n.message.DefaultSimpleMessageResolver;
import com.cb.geemvc.i18n.message.SimpleMessageResolver;
import com.cb.geemvc.intercept.*;
import com.cb.geemvc.logging.DefaultLog;
import com.cb.geemvc.logging.Log;
import com.cb.geemvc.logging.LoggerTypeListener;
import com.cb.geemvc.matcher.*;
import com.cb.geemvc.reflect.DefaultReflectionProvider;
import com.cb.geemvc.reflect.DefaultReflectionsWrapper;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.cb.geemvc.reflect.ReflectionsWrapper;
import com.cb.geemvc.script.*;
import com.cb.geemvc.validation.*;
import com.cb.geemvc.view.*;
import com.cb.geemvc.view.bean.DefaultView;
import com.cb.geemvc.view.bean.View;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class GeemvcModule extends AbstractModule {

    @Override
    protected void configure() {
        configureLog();
        configureLoggerTypeListener();
        configureConfiguration();
        configureCache();
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
        configureRequestHandlerKey();
        configureRequestMappingKey();
        configureCompositeHandlerResolver();
        configureSimpleHandlerResolver();
        configureHandlerResolverStats();
        configureEvaluatorFactory();
        configureEvaluatorContext();
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

        configureStreamViewHandler();

        configureError();
        configureErrors();
        configureValidator();
        configureValidation();
        configureValidations();
        configureValidationContext();
        configureValidationAdapterKey();
        configureValidationAdapterFactory();
        configureBindings();
        configureViewOnlyRequestHandler();
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
        bind(com.cb.geemvc.validation.Error.class).to(DefaultError.class);
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

    protected void configureConfiguration() {
        bind(Configuration.class).to(DefaultConfiguration.class);
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
        bind(HandlerResolverStats.class).to(DefaultHandlerResolverStats.class);
    }

    protected void configureRequestRunner() {
        bind(RequestRunner.class).to(DefaultRequestRunner.class);
    }

    protected void configureRequestContext() {
        bind(RequestContext.class).to(DefaultRequestContext.class);
    }

    protected void configurePathOnlyRequestContext() {
        bind(PathOnlyRequestContext.class).to(DefaultPathOnlyRequestContext.class);
    }

    protected void configureCache() {
        bind(Cache.class).to(DefaultCache.class);
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
        bind(View.class).to(DefaultView.class);
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
}
