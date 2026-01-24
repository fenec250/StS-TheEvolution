package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

public class CrystalShard
		extends BaseEvoCard implements CustomSavable<Integer>, GlowingCard {
	public static final String ID = "evolutionmodV2:CrystalShard";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalShard.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 6;
	private static final int UPGRADE_DAMAGE_AMT = 3;
	private static final int FORM_DAMAGE = 4;
	private static final int UPGRADE_FORM_DAMAGE = 0;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalShard() {
		this(-1);
	}

	public CrystalShard(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.damage = this.baseDamage= DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		if (geneIndexes == -1 && CardCrawlGame.isInARun() && AbstractDungeon.miscRng != null) {
			geneIndexes = AbstractDungeon.miscRng.random(11 * 10 - 1);
			if (geneIndexes/11==geneIndexes%11) // avoids duplicates when creating an instance without specific genes
				geneIndexes = 110+geneIndexes/11;
		}
		this.genesIndexes = geneIndexes;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int damage = this.damage;

		addToBot(new DamageAction(
				m, new DamageInfo(p, damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		BaseEvoCard.formEffect(this.firstGene.ID); // effect applied in calculateBlock
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
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return firstGene != null && secondGene != null;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				if (isPlayerInThisForm(firstGene.ID)) {
					switch (firstGene.ID) {
						case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
						case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
						case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
						case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
						case LizardGene2.ID: return LizardGene2.COLOR.cpy();
						case BeastGene2.ID: return BeastGene2.COLOR.cpy();
						case PlantGene2.ID: return PlantGene2.COLOR.cpy();
						case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
						case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
						case InsectGene2.ID: return InsectGene2.COLOR.cpy();
						case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
					}
				}
				return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				if (isPlayerInThisForm(secondGene.ID, firstGene.ID)) {
					switch (secondGene.ID) {
						case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
						case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
						case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
						case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
						case LizardGene2.ID: return LizardGene2.COLOR.cpy();
						case BeastGene2.ID: return BeastGene2.COLOR.cpy();
						case PlantGene2.ID: return PlantGene2.COLOR.cpy();
						case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
						case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
						case InsectGene2.ID: return InsectGene2.COLOR.cpy();
						case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
					}
				}
				return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public void resetGene() {
		if (this.genesIndexes < 0 || this.genesIndexes > 11 * 11 - 1) {
			return;
		}
		this.firstGene =  getGene(GeneIds[this.genesIndexes / 11]);
		this.secondGene = getGene(GeneIds[this.genesIndexes % 11]);
		this.rawDescription = EXTENDED_DESCRIPTION[0]
				+ this.firstGene.ID + EXTENDED_DESCRIPTION[1]
				+ this.secondGene.ID + EXTENDED_DESCRIPTION[2];
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