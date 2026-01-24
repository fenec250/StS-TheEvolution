package evolutionmod.cardsV1;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.orbsV1.*;

public class Adaptation
        extends AdaptableEvoCard implements GlowingCard {
    public static final String cardID = "Adaptation";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Adaptation.png";
    private static final int COST = -1;

    public Adaptation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if (!upgraded) {
            this.adapt(x);
        } else {
            this.chooseAndAdapt(x);
//            addToBot(new ChooseAdaptationAction(energyOnUse, this));
        }
        this.useAdaptations(p, m);
        if (!freeToPlayOnce) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    p.energy.use(energyOnUse);
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.amount;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.updateDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return EnergyPanel.getCurrentEnergy();
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return upgraded
                ? AbstractDungeon.player.orbs.stream()
                    .filter(o -> this.canAdaptWith(o) > 0)
                    .skip(glowIndex)
                    .findFirst().isPresent()
                : AbstractDungeon.player.orbs.stream()
                    .anyMatch(o -> this.canAdaptWith(o) > 0);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return upgraded
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractDungeon.player.orbs.stream()
                .filter(o -> this.canAdaptWith(o) > 0)
                .skip(glowIndex).findFirst()
                .map(o -> {
                    switch (o.ID) {
                        case HarpyGene.ID: return HarpyGene.COLOR.cpy();
                        case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
                        case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
                        case CentaurGene.ID: return CentaurGene.COLOR.cpy();
                        case LizardGene.ID: return LizardGene.COLOR.cpy();
                        case BeastGene.ID: return BeastGene.COLOR.cpy();
                        case PlantGene.ID: return PlantGene.COLOR.cpy();
                        case ShadowGene.ID: return ShadowGene.COLOR.cpy();
                        case LymeanGene.ID: return LymeanGene.COLOR.cpy();
                        case InsectGene.ID: return InsectGene.COLOR.cpy();
                        case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
                        default: return null;
                    }
                }).orElse(AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy());
    }
}