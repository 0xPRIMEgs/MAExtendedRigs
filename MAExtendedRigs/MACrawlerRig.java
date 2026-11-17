package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** A segmented body with configurable walking-leg pairs per segment. */
public final class MACrawlerRig extends MAExtendedRig {
    public static final int TYPE=1006;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(3,.45f,.3f).bodySegments(8).legPairs(1).legs(.12f,.18f,.08f).legSplay(.2f).tail(0,1).build();}
    public MACrawlerRig(){this(defaultConfig());}
    public MACrawlerRig(MARigConfig config){super(TYPE,Layouts.crawler(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MACrawlerRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MACrawlerRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

