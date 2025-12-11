package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;

public class PoisonFangs
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PoisonFangs";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 7;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int LIZARD_POISON_AMT = 3;
    private static final int UPGRADE_LIZARD_POISON_AMT = 1;

    public PoisonFangs() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = LIZARD_POISON_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        if (!this.upgraded) {
            formEffect(LizardGene.ID, () -> addToBot(new ApplyPowerAction(
                    m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber)));
        } else {
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
            addToBot(new LizardGene().getChannelAction());
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
//            this.upgradeMagicNumber(UPGRADE_LIZARD_POISON_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (!this.upgraded && isPlayerInThisForm(LizardGene.ID)) {
            this.glowColor = LizardGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}