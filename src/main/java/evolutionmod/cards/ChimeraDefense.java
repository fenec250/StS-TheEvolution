package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

public class ChimeraDefense
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmodV2:ChimeraDefense";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ChimeraDefense.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 6;
    private static final int UPGRADE_BLOCK_AMT = 2;
    private static final int ADAPT_MAX_AMT = 4;
    private static final int UPGRADE_ADAPT_MAX_AMT = 2;

    public ChimeraDefense() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ADAPT_MAX_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        this.adapt(1);
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return Math.min(adaptation.amount, this.magicNumber - adaptationMap.values().stream().mapToInt(a -> a.amount).sum());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_ADAPT_MAX_AMT);
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
}