package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class LoyalWarrior
		extends BaseEvoCard implements CustomSavable<Integer> {
	public static final String ID = "evolutionmod:LoyalWarrior";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 2;
	private static final int DAMAGE_AMT = 12;
	private static final int UPGRADE_DAMAGE_AMT = 3;
	private static final int STRENGTH_AMT = 1;
	private static final int UPGRADE_STRENGTH_AMT = 1;

	private int geneIndex;
	private AbstractGene gene;

	public LoyalWarrior() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.ENEMY);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = STRENGTH_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalWarrior(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = STRENGTH_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.geneIndex < 0) {
			resetGene();
		}
		addToBot(new DamageAction(
				m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));

		if (AbstractGene.isPlayerInThisForm(this.gene.ID)) {
			addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber)));
		} else {
			addToBot(new ChannelAction(this.gene.makeCopy()));
		}
	}


	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new LoyalWarrior() : new LoyalWarrior(this.geneIndex);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
			this.upgradeMagicNumber(UPGRADE_STRENGTH_AMT);
		}
	}

	private void resetGene() {
		if (this.geneIndex < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.geneIndex = AbstractDungeon.miscRng.random(8 - 1);
		}
		AbstractGene[] validGenes = {
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new BeastGene(),
				new LizardGene(),
				new ShadowGene()};
		this.gene = validGenes[this.geneIndex];
		this.rawDescription = DESCRIPTION + this.gene.name + EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.geneIndex;
	}

	@Override
	public void onLoad(Integer integer) {
		this.geneIndex = integer;
		resetGene();
	}
}