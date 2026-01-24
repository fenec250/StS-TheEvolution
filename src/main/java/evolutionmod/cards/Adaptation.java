package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import evolutionmod.orbs.*;
import evolutionmod.orbsV1.*;
import evolutionmod.patches.EvolutionEnum;

public class Adaptation
        extends AdaptableEvoCard implements GlowingCard, StartupCard {
    public static final String ID = "evolutionmodV2:Adaptation";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Adaptation.png";
    private static final int COST = -1;
    private static final int START_ADAPT = 1;
    private static final int UPGRADE_START_ADAPT = 1;

    public Adaptation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = START_ADAPT;
        this.updateDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        this.adapt(x);
//        this.chooseAndAdapt(x);
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
    public boolean atBattleStartPreDraw() {
        for (int i = 0; i < this.magicNumber; ++i) {
            String geneId = GeneIds[AbstractDungeon.cardRandomRng.random(GeneIds.length - 1)];
            AbstractAdaptation adaptation = null;
            switch(geneId) {
                case HarpyGene2.ID: adaptation = this.adaptationMap.getOrDefault(HarpyGene2.ID, new HarpyGene2.Adaptation(0)); break;
                case LymeanGene2.ID: adaptation = this.adaptationMap.getOrDefault(LymeanGene2.ID, new LymeanGene2.Adaptation(0)); break;
                case InsectGene2.ID: adaptation = this.adaptationMap.getOrDefault(InsectGene2.ID, new InsectGene2.Adaptation(0)); break;
                case PlantGene2.ID: adaptation = this.adaptationMap.getOrDefault(PlantGene2.ID, new PlantGene2.Adaptation(0)); break;
                case CentaurGene2.ID: adaptation = this.adaptationMap.getOrDefault(CentaurGene2.ID, new CentaurGene2.Adaptation(0)); break;
                case BeastGene2.ID: adaptation = this.adaptationMap.getOrDefault(BeastGene2.ID, new BeastGene2.Adaptation(0)); break;
                case SuccubusGene2.ID: adaptation = this.adaptationMap.getOrDefault(SuccubusGene2.ID, new SuccubusGene2.Adaptation(0)); break;
                case LizardGene2.ID: adaptation = this.adaptationMap.getOrDefault(LizardGene2.ID, new LizardGene2.Adaptation(0)); break;
                case ShadowGene2.ID: adaptation = this.adaptationMap.getOrDefault(ShadowGene2.ID, new ShadowGene2.Adaptation(0)); break;
                case LavafolkGene2.ID: adaptation = this.adaptationMap.getOrDefault(LavafolkGene2.ID, new LavafolkGene2.Adaptation(0)); break;
                case MerfolkGene2.ID: adaptation = this.adaptationMap.getOrDefault(MerfolkGene2.ID, new MerfolkGene2.Adaptation(0)); break;
            }
            if (adaptation != null) {
                adaptation.max += 1;
                adaptation.amount += 1;
                this.adaptationMap.put(geneId, adaptation);
            }
        }
        this.updateDescription();
        return false;
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.amount;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_START_ADAPT);
            this.updateDescription();
        }
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = this.magicNumber == 1 ? DESCRIPTION
                : EXTENDED_DESCRIPTION[0] + this.magicNumber + EXTENDED_DESCRIPTION[1];
        super.initializeDescription();
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
        return AbstractDungeon.player.orbs.stream()
                .filter(o -> this.canAdaptWith(o) > 0)
                .skip(glowIndex).findFirst()
                .map(o -> {
                    switch (o.ID) {
                        case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
                        case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
                        case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
                        case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
                        case LizardGene2.ID: return LizardGene2.COLOR.cpy();
                        case BeastGene2.ID: return BeastGene2.COLOR.cpy();
                        case PlantGene2.ID: return PlantGene2.COLOR.cpy();
                        case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
                        case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
                        case InsectGene2.ID: return InsectGene2.COLOR.cpy();
                        case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
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