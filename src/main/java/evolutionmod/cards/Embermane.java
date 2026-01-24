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
import evolutionmod.orbs.LavafolkGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.BestialRagePower;

public class Embermane
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Embermane";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Embermane.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int POWER_AMT = 3;
    private static final int UPGRADE_POWER_AMT = 1;

    public Embermane() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        int energy = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == CardType.ATTACK
                        && c != this)
                .mapToInt(c -> c.cost >= 0 ? c.cost : c.energyOnUse)
                .sum();
        while (energy > 0) {
            addToBot(new LavafolkGene2().getChannelAction());
            --energy;
        }
        addToBot(new ApplyPowerAction(p, p, new BestialRagePower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Embermane();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_POWER_AMT);
        }
    }
}