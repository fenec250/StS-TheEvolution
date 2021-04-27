package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

import java.util.ArrayList;
import java.util.List;

public class Ritual
        extends AdaptableEvoCard implements CustomSavable<Integer> {
    public static final String ID = "evolutionmod:Ritual";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Ritual.png";
    private static final int COST = 1;
    private static final int ADAPT_MAX_AMT = 1;
    private static final int ADAPT_AMT = 1;
    private static final int UPGRADE_ADAPT_AMT = 1;

    public Ritual() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.tags.add(CardTags.HEALING);
        this.exhaust = true;
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
//        p.orbs.stream()
//                .filter(o -> this.canAdaptWith(o) > 0)
//                .findAny()
//                .ifPresent(o -> {
//                    int adapted = this.tryAdaptingWith((AbstractGene) o, true);
//                });
        this.adapt(1);
        this.useAdaptations(p, m);
    }

    @Override
    protected void addAdaptation(AbstractAdaptation adaptation) {
        super.addAdaptation(adaptation);
        AbstractDungeon.player.masterDeck.group.stream()
                .filter(c -> c.uuid.equals(this.uuid))
                .findAny()
                .ifPresent(c -> {
                    Ritual ritual = (Ritual) c;
                    ritual.adaptationMap.get(adaptation.getGeneId()).amount = adaptation.amount;
                    ritual.updateDescription();
                });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.isEthereal = true;
//            this.upgradeMagicNumber(UPGRADE_ADAPT_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.updateDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.orbs.stream().anyMatch(o -> this.canAdaptWith(o) > 0)) {
            this.glowColor = AbstractDungeon.player.orbs.stream()
                    .filter(o -> this.canAdaptWith(o) > 0)
                    .findFirst()
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
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

//    @Override
//    public void triggerOnGlowCheck() {
//        if (AbstractDungeon.player.orbs.stream().anyMatch(o -> this.canAdaptWith(o) > 0)) {
//            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        } else {
//            this.glowColor = ;
//        }
//    }

    @Override
    public Integer onSave() {
        List<AbstractAdaptation> adaptations = new ArrayList<>(this.adaptationMap.values());
        int bitmask = 0;
        for (int i = 0; i < adaptations.size(); ++i) {
            if (adaptations.get(i).amount > 0) {
                bitmask += 1 << i;
            }
        }
        return bitmask;
    }

    @Override
    public void onLoad(Integer integer) {
        List<AbstractAdaptation> adaptations = new ArrayList<>(this.adaptationMap.values());
        for (int i = 0; i < adaptations.size(); ++i) {
            adaptations.get(i).amount = (integer >> i) & 1;
        }
        this.updateDescription();
    }
}