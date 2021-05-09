package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.AbstractGene;

public class MasteryPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:MasteryPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    private String geneId;
    private String geneName;

    public MasteryPower(AbstractCreature owner, int initialAmount, String geneId) {
        this.geneId = geneId;
        this.geneName = BaseEvoCard.replaceGeneIds(geneId);
        this.name = nameForGene(geneName);
        this.ID = powerIdForGene(geneId);
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 32.png"), 0, 0, 32, 32);
        this.loadRegion("mental_fortress");
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[1] + this.geneName + (this.amount == 1 ? DESCRIPTIONS[2]
                : DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4]);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        if (orb instanceof AbstractGene && geneId.equals(orb.ID)) {
            AdaptableEvoCard.AbstractAdaptation adaptation = ((AbstractGene) orb).getAdaptation();
            adaptation.amount = amount;
            adaptation.apply(AbstractDungeon.player, null);
            this.flash();
        }
    }

    public static String nameForGene(String geneName) {
        return geneName + DESCRIPTIONS[0];
    }

    public static String powerIdForGene(String geneId) {
        return POWER_ID + geneId;
    }
}
