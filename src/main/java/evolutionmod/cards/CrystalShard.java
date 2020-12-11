package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;

public class CrystalShard
		extends BaseEvoCard implements CustomSavable<Integer> {
	public static final String ID = "evolutionmod:CrystalShard";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 9;
	private static final int UPGRADE_DAMAGE_AMT = 2;
	private static final int FORM_DAMAGE = 4;
	private static final int UPGRADE_FORM_DAMAGE = 1;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalShard() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		this.genesIndexes = -1;
		resetGene();
	}

	private CrystalShard(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.damage = this.baseDamage= DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		this.genesIndexes = geneIndexes;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int damage = this.damage;

		BaseEvoCard.formEffect(this.firstGene.ID); // effect applied in calculateDamage
		addToBot(new DamageAction(
				m, new DamageInfo(p, damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		BaseEvoCard.formEffect(this.secondGene.ID, () -> addToBot(new DrawCardAction(p, 1)));
	}

	@Override
	public void applyPowers() {
		this.calculateDamage();
		super.applyPowers();
		this.baseDamage = getBaseDamage();
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		this.calculateDamage();
		super.calculateCardDamage(mo);
		this.baseDamage = getBaseDamage();
		this.isDamageModified = this.damage != this.baseDamage;
	}

	public int getBaseDamage() {
		return DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
	}

	public void calculateDamage() {
		this.baseDamage = getBaseDamage();
		if (BaseEvoCard.isPlayerInThisForm(this.firstGene.ID)) {
			this.baseDamage += this.magicNumber;
		}
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.secondGene == null ? new CrystalShard() : new CrystalShard(this.genesIndexes);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
			this.upgradeMagicNumber(UPGRADE_FORM_DAMAGE);
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized forms",
					"The forms on this card are selected when the card is created and vary from a card to an other."));
		}
		return  customTooltips;
	}

	private void resetGene() {
		if (this.genesIndexes < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.genesIndexes = AbstractDungeon.miscRng.random(11 * 10 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene(),
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new InsectGene(),
				new BeastGene(),
				new LizardGene(),
				new CentaurGene(),
				new ShadowGene()};
		this.firstGene =  validGenes[this.genesIndexes / 11];
		this.secondGene = validGenes[this.genesIndexes / 11 == this.genesIndexes % 10 ? 10 : this.genesIndexes % 10];
		this.rawDescription = DESCRIPTION
				+ this.firstGene.getColoredName() + EXTENDED_DESCRIPTION[1]
				+ this.secondGene.getColoredName() + EXTENDED_DESCRIPTION[2];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.genesIndexes;
	}

	@Override
	public void onLoad(Integer integer) {
		this.genesIndexes = integer;
		resetGene();
	}
}