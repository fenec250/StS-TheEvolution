package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

import java.util.ArrayList;
import java.util.List;

public class Ritual
        extends AdaptableEvoCard implements CustomSavable<Integer> {
    public static final String ID = "evolutionmodV2:Ritual";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Ritual.png";
    private static final int COST = 1;
    private static final int ADAPT_MAX_AMT = 1;
    private static final int ADAPT_AMT = 1;

    public Ritual() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.tags.add(CardTags.HEALING);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = ADAPT_AMT;
        this.adaptationMap.put(HarpyGene2.ID, new HarpyGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LymeanGene2.ID, new LymeanGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(InsectGene2.ID, new InsectGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(PlantGene2.ID, new PlantGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(CentaurGene2.ID, new CentaurGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(BeastGene2.ID, new BeastGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(SuccubusGene2.ID, new SuccubusGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LizardGene2.ID, new LizardGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(ShadowGene2.ID, new ShadowGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(LavafolkGene2.ID, new LavafolkGene2.Adaptation(0, ADAPT_MAX_AMT));
        this.adaptationMap.put(MerfolkGene2.ID, new MerfolkGene2.Adaptation(0, ADAPT_MAX_AMT));
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