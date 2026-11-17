package MAExtendedRigs;

/**
 * Reusable starting proportions/topologies, not finished character meshes or
 * authored locomotion. config() returns an immutable configuration; customize
 * through toBuilder(). All create calls return independent rigs.
 */
public enum MARigPreset {
    BIPED, HOPPER, QUADRUPED, HOOFED, HEAVY, REPTILE, FROG, WINGED_QUADRUPED,
    BIRD, BAT, INSECT, FLYING_INSECT, CRAB, CENTIPEDE, ISOPOD, SERPENT,
    FISH, RAY, SEAHORSE, WALRUS, TENTACLED, SNAIL, PLANT, SPIRIT;

    public MARigConfig config(){
        return switch(this){
            case BIPED -> MABipedRig.defaultConfig();
            case HOPPER -> MABipedRig.defaultConfig().toBuilder().body(.6f,.6f,.8f).armLength(.55f)
                .legs(.75f,.65f,.65f).tail(5,1.8f).build();
            case QUADRUPED -> MAQuadrupedRig.defaultConfig();
            case HOOFED -> MAQuadrupedRig.defaultConfig().toBuilder().body(1.25f,.6f,.65f)
                .neck(2,.85f).legs(.8f,.8f,.22f).tail(2,.45f).build();
            case HEAVY -> MAQuadrupedRig.defaultConfig().toBuilder().body(1.8f,1.25f,1.1f)
                .neck(1,.25f).legs(.65f,.55f,.45f).tail(3,.6f).build();
            case REPTILE -> MAQuadrupedRig.defaultConfig().toBuilder().body(1.6f,.65f,.35f)
                .neck(2,.2f).legs(.18f,.22f,.3f).legSplay(.4f).tail(6,1.8f).build();
            case FROG -> MAQuadrupedRig.defaultConfig().toBuilder().body(.6f,.9f,.45f)
                .neck(0,.1f).legs(.2f,.3f,.45f).legSplay(.35f).tail(0,1).build();
            case WINGED_QUADRUPED -> MAQuadrupedRig.defaultConfig().toBuilder()
                .neck(3,.8f).tail(6,1.8f).wings(1,3,1.8f).wingDigits(3).build();
            case BIRD -> MABirdRig.defaultConfig();
            case BAT -> MABirdRig.defaultConfig().toBuilder().body(.6f,.45f,.5f)
                .neck(1,.15f).legs(.18f,.2f,.15f).wings(1,2,1.1f).wingDigits(4).tail(1,.3f).build();
            case INSECT -> MAInsectRig.defaultConfig();
            case FLYING_INSECT -> MAInsectRig.defaultConfig().toBuilder()
                .wings(2,2,1.1f).legs(.15f,.2f,.1f).legSplay(.15f).build();
            case CRAB -> MACrustaceanRig.defaultConfig();
            case CENTIPEDE -> MACrawlerRig.defaultConfig();
            case ISOPOD -> MACrawlerRig.defaultConfig().toBuilder().body(1.4f,.85f,.6f)
                .bodySegments(7).legs(.1f,.12f,.1f).legSplay(.18f).build();
            case SERPENT -> MASerpentRig.defaultConfig();
            case FISH -> MAFishRig.defaultConfig();
            case RAY -> MAFishRig.defaultConfig().toBuilder().body(1.3f,1.2f,.2f)
                .wings(0,4,1.5f).tail(6,2).build();
            case SEAHORSE -> MAFishRig.defaultConfig().toBuilder().body(.8f,.3f,.4f)
                .neck(3,.5f).verticalBody(true).tail(8,1.2f).wings(0,2,.3f).build();
            case WALRUS -> MAQuadrupedRig.defaultConfig().toBuilder().body(1.6f,1,.75f)
                .neck(1,.2f).legs(.08f,.1f,.65f).legSplay(.3f).tail(0,1).build();
            case TENTACLED -> MATentacleRig.defaultConfig();
            case SNAIL -> MASnailRig.defaultConfig();
            case PLANT -> MAPlantRig.defaultConfig();
            case SPIRIT -> MASpiritRig.defaultConfig();
        };
    }
    public MAExtendedRig create(){return create(config());}
    public MAExtendedRig create(MARigConfig c){
        return switch(this){
            case BIPED,HOPPER -> new MABipedRig(c);
            case QUADRUPED,HOOFED,HEAVY,REPTILE,FROG,WINGED_QUADRUPED,WALRUS -> new MAQuadrupedRig(c);
            case BIRD,BAT -> new MABirdRig(c);
            case INSECT,FLYING_INSECT -> new MAInsectRig(c);
            case CRAB -> new MACrustaceanRig(c);
            case CENTIPEDE,ISOPOD -> new MACrawlerRig(c);
            case SERPENT -> new MASerpentRig(c);
            case FISH,RAY,SEAHORSE -> new MAFishRig(c);
            case TENTACLED -> new MATentacleRig(c);
            case SNAIL -> new MASnailRig(c);
            case PLANT -> new MAPlantRig(c);
            case SPIRIT -> new MASpiritRig(c);
        };
    }
}

