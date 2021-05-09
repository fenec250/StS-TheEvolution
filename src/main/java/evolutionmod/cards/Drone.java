package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.powers.BroodPower;

public class Drone
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Drone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectAtt.png";
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_BLOCK_AMT = 3;

    public Drone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
        this.isEthereal = true;
    }

    public static Drone createDroneWithInteractions(AbstractPlayer player) {
        return new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            if (this.block > 0) {
                addToBot(new GainBlockAction(p, this.block));
            }
        } else {
            addToBot(new DamageAction(
                    m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
	public void applyPowers() {
		applyBroodPowerAround(super::applyPowers);
    }

    @Override
	public void calculateCardDamage(AbstractMonster mo) {
		applyBroodPowerAround(() -> super.calculateCardDamage(mo));
    }

    public void applyBroodPowerAround(Runnable supercall) {
		int baseDamage = this.baseDamage;
		int baseBlock = this.baseBlock;
		AbstractPower power = AbstractDungeon.player.getPower(BroodPower.POWER_ID);
		int pot = power == null ? 0 : power.amount;
		this.baseDamage += pot;
		this.baseBlock += pot;

		supercall.run();

		this.baseDamage = baseDamage;
		this.isDamageModified = this.baseDamage != this.damage;

		this.baseBlock = baseBlock;
		this.isBlockModified = this.baseBlock != this.block;
	}

	@Override
	public void triggerWhenDrawn() {
		this.addToBot(new SetDontTriggerAction(this, false));
	}

	public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        this.applyPowersToBlock();
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}