package evolutionmod.cardsV1;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbsV1.BeastGene;
import evolutionmod.orbsV1.SuccubusGene;
import evolutionmod.patches.EvolutionEnum;

public class StripArmor
        extends BaseEvoCard implements GlowingCard {
    public static final String cardID = "StripArmor";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/StripArmor.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 5;
    private static final int UPGRADE_DAMAGE_AMT = 2;

    public StripArmor() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

		if (isPlayerInThisForm(SuccubusGene.ID)) {
			addToBot(new RemoveAllBlockAction(m, p));
		}
		AbstractDungeon.actionManager.addToBottom(new DamageAction(
				m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		formEffect(SuccubusGene.ID);
		formEffect(BeastGene.ID);
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
		if (BaseEvoCard.isPlayerInThisForm(BeastGene.ID)) {
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
        }
    }

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return true;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				return isPlayerInThisForm(SuccubusGene.ID) ? SuccubusGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				return isPlayerInThisForm(BeastGene.ID, SuccubusGene.ID) ? BeastGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}