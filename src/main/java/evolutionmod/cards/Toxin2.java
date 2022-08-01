package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;

public class Toxin2
		extends AdaptableEvoCard {
	public static final String ID = "evolutionmod:Toxin";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Toxin.png";
	private static final int COST = 1;
	private static final int POISON_AMT = 3;
	private static final int UPGRADE_POISON_AMT = 1;

	public Toxin2() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.ENEMY);
		this.magicNumber = this.baseMagicNumber = POISON_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int poison = this.magicNumber;
		this.exhaust = false;
		boolean inForm = isPlayerInThisForm(PlantGene.ID);
		if (inForm) {
			poison += 2*this.magicNumber;
			this.exhaust = true;
		}
		addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poison)));
		formEffect(PlantGene.ID);
	}

	@Override
	public AbstractCard makeCopy() {
		return new Toxin2();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_POISON_AMT);
//			this.rawDescription = UPGRADE_DESCRIPTION;
//			this.initializeDescription();
		}
	}

	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		if (isPlayerInThisForm(PlantGene.ID)) {
			this.glowColor = PlantGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}
