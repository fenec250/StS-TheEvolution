package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import evolutionmod.actions.ChooseAdaptationAction;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class Adaptation
        extends AdaptableEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Adaptation";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = -1;

    public Adaptation() {
        super(ID, NAME, new RegionName("red/skill/ghostly_armor"), COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
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
            this.initialRawDescription = UPGRADE_DESCRIPTION;
            this.updateDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return EnergyPanel.getCurrentEnergy();
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return AbstractDungeon.player.orbs.stream()
                .filter(o -> this.canAdaptWith(o) > 0)
                .skip(glowIndex)
                .findFirst().isPresent();
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return AbstractDungeon.player.orbs.stream()
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