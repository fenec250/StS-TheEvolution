package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;

public class PowerFocus extends CustomRelic {
    public static final String ID = "evolutionmod:PowerFocus";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/PowerFocus.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/PowerFocus_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);

    public PowerFocus() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.pulse = true;
    }

    @Override
    public void atTurnStart() {
        this.pulse = true;
    }

    @Override
    public void onEvokeOrb(AbstractOrb ammo) {
        super.onEvokeOrb(ammo);
        if (this.pulse && ammo instanceof AbstractGene) {
            ammo.onEvoke();
            this.pulse = false;
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.pulse = false;
    }
}