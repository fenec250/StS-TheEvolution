package evolutionmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.AbstractGene;

public class AbsorptionPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:AbsorptionPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    private AbstractGene gene;
    private String geneName;

    public AbsorptionPower(AbstractCreature owner, int initialAmount, AbstractGene gene) {
//        this.geneId = geneId;
        this.gene = gene;
        this.geneName = BaseEvoCard.replaceGeneIds(gene.ID);
        this.name = nameForGene(geneName);
        this.ID = powerIdForGene(gene.ID);
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 32.png"), 0, 0, 32, 32);
        this.loadRegion("focus");
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
    public void atStartOfTurn() {
        for(int i=0; i<this.amount; ++i) {
            gene.onStartOfTurn();
            gene.onEndOfTurn();
        }
        super.atStartOfTurn();
    }

    public static String nameForGene(String geneName) {
        return DESCRIPTIONS[0] + geneName;
    }

    public static String powerIdForGene(String geneId) {
        return POWER_ID + geneId;
    }

//    public static String powerImageForGene(String geneId) {
//
//    }
}
