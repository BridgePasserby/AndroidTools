package gcy.androidtools.message.matchpolicy;


import gcy.androidtools.message.XulMessage;

import java.util.List;

public interface XulMatchPolicy {

    List<XulMessage> findMatchMessageTypes(XulMessage message);
}
