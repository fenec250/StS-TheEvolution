package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.AbstractCardEnum;

public class ChimeraStrike
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:ChimeraStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ChimeraStrike.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 7;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int ADAPT_MAX_AMT = 4;
    private static final int UPGRADE_ADAPT_MAX_AMT = 2;

    public ChimeraStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = ADAPT_MAX_AMT;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
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
                            case HarpyGene.ID: return HarpyGene.COLOR.cpy();
                            case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
                            case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
                            case CentaurGene.ID: return CentaurGene.COLOR.cpy();
                            case LizardGene.ID: return LizardGene.COLOR.cpy();
                            case BeastGene.ID: return BeastGene.COLOR.cpy();
                            case PlantGene.ID: return PlantGene.COLOR.cpy();
                            case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
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
}