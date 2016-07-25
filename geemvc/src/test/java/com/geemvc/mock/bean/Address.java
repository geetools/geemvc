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

package com.geemvc.mock.bean;

import java.util.Arrays;
import java.util.List;

public class Address {
    private Long id = null;
    private String[] streetLines = null;
    private String zip = null;
    private String city = null;
    private String countryCode = null;
    private List<AddressType> addressTypes = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getStreetLines() {
        return streetLines;
    }

    public void setStreetLines(String[] streetLines) {
        this.streetLines = streetLines;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<AddressType> getAddressTypes() {
        return addressTypes;
    }

    public void setAddressTypes(List<AddressType> addressTypes) {
        this.addressTypes = addressTypes;
    }

    @Override
    public String toString() {
        return "Address [id=" + id + ", streetLines=" + Arrays.toString(streetLines) + ", zip=" + zip + ", city=" + city + ", countryCode=" + countryCode + ", addressTypes=" + addressTypes + "]";
    }
}
