package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.LustPower;

public class CursedTouch2
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:CursedTouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CursedTouch.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int DEBUFF_AMT = 2;
    private static final int UPGRADE_DEBUFF_AMT = 1;

    public CursedTouch2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = this.damage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DEBUFF_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(m, p, new LustPower(m, this.magicNumber), this.magicNumber));
        formEffect(ShadowGene2.ID, () -> addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CursedTouch2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DEBUFF_AMT);
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(ShadowGene2.ID)) {
            this.glowColor = ShadowGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}