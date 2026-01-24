package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.PlantGene2;
import evolutionmod.patches.EvolutionEnum;

public class Toxin2
		extends AdaptableEvoCard {
	public static final String ID = "evolutionmodV2:Toxin";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Toxin.png";
	private static final int COST = 1;
	private static final int POISON_AMT = 3;
	private static final int UPGRADE_POISON_AMT = 2;
	private static final int WEAK_AMT = 2;

	public Toxin2() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.ENEMY);
		this.magicNumber = this.baseMagicNumber = POISON_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
		formEffect(PlantGene2.ID, () ->
			addToBot(new ApplyPowerAction(m, p, new WeakPower(m, WEAK_AMT, false), WEAK_AMT))
		);
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
			this.initializeDescription();
		}
	}

	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		if (isPlayerInThisForm(PlantGene2.ID)) {
			this.glowColor = PlantGene2.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}
