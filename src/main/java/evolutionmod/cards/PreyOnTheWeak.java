package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;

public class PreyOnTheWeak
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PreyOnTheWeak";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 10;
    private static final int REFUND_AMT = 1;
    private static final int UPGRADE_REFUND_AMT = 1;

    public PreyOnTheWeak() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = REFUND_AMT;
        this.isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        formEffect(LizardGene.ID, () -> addToBot(new RefundAction(this, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PreyOnTheWeak();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REFUND_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (this.upgraded && isPlayerInThisForm(LizardGene.ID)) {
            this.glowColor = LizardGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
