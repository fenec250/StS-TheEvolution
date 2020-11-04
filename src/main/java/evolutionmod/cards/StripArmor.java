package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class StripArmor
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:StripArmor";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SuccubusAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 5;
    private static final int UPGRADE_DAMAGE_AMT = 2;

    public StripArmor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION + DAMAGE_AMT + EXTENDED_DESCRIPTION[0],
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if (!AbstractGene.isPlayerInThisForm(SuccubusGene.ID)) {
			addToBot(new ChannelAction(new SuccubusGene()));
		} else {
			AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(m, p));
        }
        if (!AbstractGene.isPlayerInThisForm(BeastGene.ID)) {
			addToBot(new ChannelAction(new BeastGene()));
		}
		AbstractDungeon.actionManager.addToBottom(new DamageAction(
				m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}

	@Override
	public void applyPowers() {
    	alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
    	this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//    	this.baseMagicNumber = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//		int mn = magicNumber;
		if (AbstractGene.isPlayerInThisForm(BeastGene.ID)) {
			this.baseDamage += this.magicNumber;
//			magicNumber = 0;
//			this.isMagicNumberModified = true;
		}
    	supercall.run();
//		magicNumber = mn;
    	this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
		this.isDamageModified = this.damage != this.baseDamage;
	}

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_DAMAGE_AMT);
            this.rawDescription= DESCRIPTION + this.baseDamage + EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }
}