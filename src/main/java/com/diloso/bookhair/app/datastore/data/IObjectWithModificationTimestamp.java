package com.diloso.bookhair.app.datastore.data;

import org.joda.time.DateTime;

public interface IObjectWithModificationTimestamp {

    /**
     * @param dateTime
     */
    void setModificationTimestamp(DateTime dateTime);

    /**
     * @return modification time stamp
     */
    DateTime getModificationTimestamp();

}
