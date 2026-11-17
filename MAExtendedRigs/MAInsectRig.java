package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Thorax/abdomen, arbitrary paired legs, antennae, and optional wing pairs. */
public final class MAInsectRig extends MAExtendedRig {
    public static final int TYPE=1004;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(1.2f,.65f,.4f).legPairs(3).legs(.25f,.35f,.12f).legSplay(.45f).tail(0,1).build();}
    public MAInsectRig(){this(defaultConfig());}
    public MAInsectRig(MARigConfig config){super(TYPE,Layouts.insect(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MAInsectRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MAInsectRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

