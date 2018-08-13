package gcy.myandroidtools.message.matchpolicy;


import gcy.myandroidtools.message.XulMessage;

import java.util.List;

public interface XulMatchPolicy {

    List<XulMessage> findMatchMessageTypes(XulMessage message);
}
