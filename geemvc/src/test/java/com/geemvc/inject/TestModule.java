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

import com.geemvc.helper.DefaultTestHelper;
import com.geemvc.helper.TestHelper;
import com.geemvc.mock.bean.InheritedRootBean;
import com.geemvc.mock.bean.InheritedRootBeanImpl;
import com.geemvc.mock.bean.NestedBean;
import com.geemvc.mock.bean.NestedBeanImpl;
import com.geemvc.mock.bean.NestedFluentBean;
import com.geemvc.mock.bean.NestedFluentBeanImpl;
import com.geemvc.mock.bean.RootBean;
import com.geemvc.mock.bean.RootBeanImpl;
import com.geemvc.mock.bean.RootFluentBean;
import com.geemvc.mock.bean.RootFluentBeanImpl;
import com.geemvc.reflect.ReflectionsWrapper;
import com.geemvc.reflect.TestReflectionsWrapper;

public class TestModule extends GeemvcModule {
    @Override
    protected void configure() {
        super.configure();

        configureTestHelper();
        configureRootBean();
        configureNestedBean();
        configureRootFluentBean();
        configureNestedFluentBean();
        configureInheritedRootBean();
    }

    @Override
    protected void configureReflectionsWrapper() {
        bind(ReflectionsWrapper.class).to(TestReflectionsWrapper.class);
    }

    protected void configureTestHelper() {
        bind(TestHelper.class).to(DefaultTestHelper.class);
    }

    protected void configureRootBean() {
        bind(RootBean.class).to(RootBeanImpl.class);
    }

    protected void configureNestedBean() {
        bind(NestedBean.class).to(NestedBeanImpl.class);
    }

    protected void configureRootFluentBean() {
        bind(RootFluentBean.class).to(RootFluentBeanImpl.class);
    }

    protected void configureInheritedRootBean() {
        bind(InheritedRootBean.class).to(InheritedRootBeanImpl.class);
    }

    protected void configureNestedFluentBean() {
        bind(NestedFluentBean.class).to(NestedFluentBeanImpl.class);
    }
}
