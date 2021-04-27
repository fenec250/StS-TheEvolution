package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.SadisticPower;

public class Whip extends CustomRelic {
    public static final String ID = "evolutionmod:Whip";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/Whip.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/Whip_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int SADISTIC_AMT = 2;

    public Whip() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SADISTIC_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new SadisticPower(AbstractDungeon.player, SADISTIC_AMT)));
        this.grayscale = true;
    }
}