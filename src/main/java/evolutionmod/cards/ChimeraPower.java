package evolutionmod.cards;

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

public class ChimeraPower
        extends AdaptableEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:ChimeraPower";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Adaptation.png";
    private static final int COST = 1;
    private static final int ADAPT_AMT = 2;
    private static final int UPGRADE_ADAPT_AMT = 1;
    private static final int ADAPT_MAX_AMT = 1;

    public ChimeraPower() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ADAPT_AMT;
        this.adaptationMap.put(HarpyGene.ID, new HarpyGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LymeanGene.ID, new LymeanGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(InsectGene.ID, new InsectGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(PlantGene.ID, new PlantGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(CentaurGene.ID, new CentaurGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(BeastGene.ID, new BeastGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(SuccubusGene.ID, new SuccubusGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LizardGene.ID, new LizardGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(ShadowGene.ID, new ShadowGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LavafolkGene.ID, new LavafolkGene.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(MerfolkGene.ID, new MerfolkGene.Adaptation(0, ADAPT_MAX_AMT));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.adapt(this.magicNumber);
        this.useAdaptations(p, m);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ADAPT_AMT);
            this.updateDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return this.magicNumber;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return upgraded
                ? AbstractDungeon.player.orbs.stream()
                    .filter(o -> this.canAdaptWith(o) > 0)
                    .skip(glowIndex)
                    .findAny().isPresent()
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