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

package com.cb.geemvc.taglib.form;

import com.cb.geemvc.Char;
import com.cb.geemvc.Str;
import com.cb.geemvc.i18n.notice.Notice;
import com.cb.geemvc.i18n.notice.Notices;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Set;

public class NoticesTagSupport extends FormFieldTagSupport {
    protected String show;

    protected static final String SHOW_ALL = "all";
    protected static final String SHOW_FIELDS = "fields";
    protected static final String SHOW_GLOBAL = "global";

    protected static final String NOTICES_CONTAINER_KEY = "notices.html.container";
    protected static final String NOTICES_MESSAGE_KEY = "notices.html.message";

    @Override
    public void doTag() throws JspException, IOException {
        Notices notices = notices();

        if (notices != null && !notices.isEmpty()) {
            Set<Notice> noticeSet = noticeSet();

            if (noticeSet != null && !noticeSet.isEmpty()) {
                String containerHTML = null;
                String messageHTML = null;

                FormTagSupport formTag = formTag();

                if (formTag != null && formTag.getName() != null) {
                    // Get the HTML container to display the messages in.
                    String containerMsgKey = new StringBuilder(formTag.getName())
                            .append(Char.DOT).append(NOTICES_CONTAINER_KEY).toString();

                    containerHTML = messageResolver.resolve(containerMsgKey, requestContext(), true);

                    // Get the HTML message row to display the message in.
                    String messageMsgKey = new StringBuilder(formTag.getName())
                            .append(Char.DOT).append(NOTICES_MESSAGE_KEY).toString();

                    messageHTML = messageResolver.resolve(messageMsgKey, requestContext(), true);
                }

                // Get the standard HTML container if form specific one does not exist.
                if (containerHTML == null) {
                    containerHTML = messageResolver.resolve(NOTICES_CONTAINER_KEY, requestContext());
                }

                // Get the standard HTML message row if form specific one does not exist.
                if (messageHTML == null) {
                    messageHTML = messageResolver.resolve(NOTICES_MESSAGE_KEY, requestContext());
                }

                StringBuilder noticeMessages = new StringBuilder();

                // Iterate though all notices and append them all together in a StringBuilder.
                for (Notice notice : noticeSet) {
                    String resolvedNoticeMsg = noticeMessage(notice);

                    if (!Str.isEmpty(resolvedNoticeMsg)) {
                        noticeMessages.append(String.format(messageHTML, resolvedNoticeMsg)).append(Char.NEWLINE);
                    }
                }

                // Now add resolved messages to the notice container.
                if (noticeMessages.length() > 0) {
                    String html = String.format(containerHTML, noticeMessages.toString());
                    jspContext.getOut().write(html);
                }
            }
        }
    }

    protected Set<Notice> noticeSet() {
        Notices notices = notices();
        Set<Notice> noticeSet = null;

        if (notices != null && !notices.isEmpty()) {
            if (isShowAll()) {
                noticeSet = notices.allNotices();
            } else if (isShowFields()) {
                noticeSet = notices.fieldNotices();
            } else if (isShowGlobal()) {
                noticeSet = notices.globalNotices();
            }
        }

        return noticeSet;
    }

    protected boolean isShowAll() {
        return Str.trimEqualsIgnoreCase(SHOW_ALL, show);
    }

    protected boolean isShowFields() {
        return Str.trimEqualsIgnoreCase(SHOW_FIELDS, show);
    }

    protected boolean isShowGlobal() {
        return show == null || Str.trimEqualsIgnoreCase(SHOW_GLOBAL, show);
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
    }

    @Override
    protected void appendTagBody(JspWriter writer) throws JspException {
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
