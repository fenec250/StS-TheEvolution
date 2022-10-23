package evolutionmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.CrystalDust;
import evolutionmod.cards.CrystalShard;
import evolutionmod.cards.CrystalShield;
import evolutionmod.orbs.AbstractGene;

public class CrystalShapingPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:CrystalShapingPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public CrystalShapingPower(AbstractCreature owner, int initialAmount) {
//        this.geneId = geneId;
        this.name = NAME;
        this.ID = POWER_ID;
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
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
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
        for (int i = 0; i < this.amount; ++i) {
            int choice = AbstractDungeon.cardRandomRng.random(2);
            switch (choice) {
                case 0:
                    addToBot(new MakeTempCardInHandAction(new CrystalShard()));
                    break;
                case 1:
                    addToBot(new MakeTempCardInHandAction(new CrystalShield()));
                    break;
                case 2:
                    addToBot(new MakeTempCardInHandAction(new CrystalDust()));
                    break;
            }
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
