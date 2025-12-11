package evolutionmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.CrystalDust;
import evolutionmod.cards.CrystalShard;
import evolutionmod.cards.CrystalShield;
import evolutionmod.orbs.*;

import java.util.Iterator;
import java.util.Optional;

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

    private int geneInt(AbstractOrb gene) {
        switch (gene.ID) { // should match BaseEvoCard.GeneIds indexes
            case PlantGene.ID: return 0;
            case MerfolkGene.ID: return 1;
            case HarpyGene.ID: return 2;
            case LavafolkGene.ID: return 3;
            case SuccubusGene.ID: return 4;
            case LymeanGene.ID: return 5;
            case InsectGene.ID: return 6;
            case BeastGene.ID: return 7;
            case LizardGene.ID: return 8;
            case CentaurGene.ID: return 9;
            case ShadowGene2.ID: return 10;
        }
        return -1;
    }
    @Override
    public void atStartOfTurn() {
        Iterator<AbstractOrb> genes = ((AbstractPlayer) this.owner).orbs.stream()
                .filter(orb -> orb instanceof AbstractGene)
                .iterator();
        for (int i = 0; i < this.amount; ++i) {
            BaseEvoCard card;
            int choice = AbstractDungeon.cardRandomRng.random(2);
            switch (choice) {
                case 0:
                    card = new CrystalShard(
                            genes.hasNext() ? (geneInt(genes.next())*11 + (genes.hasNext() ? (geneInt(genes.next()))
                                    : AbstractDungeon.cardRandomRng.random(10 - 1)))
                                    : AbstractDungeon.cardRandomRng.random(11 * 10 - 1)
                    );
                    break;
                case 1:
                    card = new CrystalShield(
                            genes.hasNext() ? (geneInt(genes.next())*11 + (genes.hasNext() ? (geneInt(genes.next()))
                                    : AbstractDungeon.cardRandomRng.random(10 - 1)))
                                    : AbstractDungeon.cardRandomRng.random(11 * 10 - 1)
                    );
                    break;
                case 2:
                default:
                    card = new CrystalDust(
                            genes.hasNext() ? geneInt(genes.next())
                                    : AbstractDungeon.cardRandomRng.random(11 - 1)
                    );
                    break;
            }
            addToBot(new MakeTempCardInHandAction(card));
        }
        super.atStartOfTurn();
    }
}
